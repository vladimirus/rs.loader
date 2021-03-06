package rs.loader.job;

import static com.github.jreddit.retrieval.params.SubmissionSort.HOT;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.rangeClosed;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.loader.model.Link;
import rs.loader.model.Topic;
import rs.loader.service.SimpleManager;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Executor;

@Service
public class LinkLoaderJob extends AbstractLoaderJob<Submission, Link> {
    static int IDLE_SLEEP_IN_SECONDS = 5;
    static int ERROR_SLEEP_IN_SECONDS = 10;

    private Logger log = Logger.getLogger(LinkLoaderJob.class);
    @Autowired
    private Submissions submissions;
    @Autowired
    private Converter<Submission, Link> linkConverter;
    @Autowired
    private Validator<Link> linkValidator;
    @Autowired
    private SimpleManager<Link> linkManager;
    @Autowired
    private CommentLoaderJob commentLoaderJob;
    @Autowired
    private AsyncEventBus eventBus;
    @Autowired
    @Qualifier(value = "linkExecutor")
    private Executor executor;

    Queue<String> queue = new LinkedList<>();

    @Scheduled(initialDelay = 10000, fixedRate = 1000)
    public void scheduledLoad() {
        executor.execute(this::load);
    }

    public void load() {
        gaugeService.submit("loader.link.queue-size", queueSize());
        if (readyToRun(commentLoaderJob.queueSize())) {

            StopWatch timer = new StopWatch();
            timer.start();

            ofNullable(queue.poll())
                    .flatMap(topicDisplayName -> process(topicDisplayName, 100))
                    .filter(links -> !links.isEmpty())
                    .ifPresent(links -> {
                        timer.stop();
                        linkManager.save(links);
                        log.info(format("%20s: saved %3d links; took: %s", links.stream().findAny().get().getTopic(), links.size(), timer.toString()));
                        links.stream().forEach(eventBus::post);
                    });

            if (queue.isEmpty()) {
                log.info(format("Link queue is empty, utilise it more? Sleeping for %d seconds...", IDLE_SLEEP_IN_SECONDS));
                sleepUninterruptibly(IDLE_SLEEP_IN_SECONDS, SECONDS);
            }
        }
    }

    Optional<Collection<Link>> process(String topicDisplayName, int maxAttempts) {
        Optional<Collection<Link>> retrievedLinks = rangeClosed(1, maxAttempts)
                .mapToObj(i -> {
                    try {
                        return load(submissions.ofSubreddit(topicDisplayName, HOT, -1, 100, null, null, true).stream(), linkConverter, linkValidator);
                    } catch (Exception ignore) {
                        if (ignore.getMessage().contains("HTTP Error (403)")) {
                            log.info(format("Error retrieving links: iteration: %d, topic: %s. Topic is private, ignoring", i, topicDisplayName));
                            return Collections.<Link>emptyList();
                        }
                        log.info(format("Error retrieving links: iteration: %d, topic: %s. Sleeping for %d seconds then trying again", i, topicDisplayName, i * ERROR_SLEEP_IN_SECONDS));
                        counterService.increment(format("loader.%s.sleeping", this.getClass().getSimpleName().toLowerCase()));
                        sleepUninterruptibly(i * ERROR_SLEEP_IN_SECONDS, SECONDS);
                        counterService.decrement(format("loader.%s.sleeping", this.getClass().getSimpleName().toLowerCase()));
                        return null;
                    }
                })
                .filter(links -> links != null)
                .findAny();

        if (retrievedLinks.isPresent() && retrievedLinks.get().isEmpty()) {
            log.info(format("%20s: retrieved %3d links", topicDisplayName, 0));
        }

        return retrievedLinks;
    }

    @Subscribe
    public void handle(Topic topic) {
        queue.add(topic.getDisplayName());
    }

    public int queueSize() {
        return queue.size();
    }
}