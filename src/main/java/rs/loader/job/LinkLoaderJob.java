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
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.loader.model.Link;
import rs.loader.model.Topic;
import rs.loader.service.SimpleManager;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

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
    private GaugeService gaugeService;
    @Autowired
    private CommentLoaderJob commentLoaderJob;
    @Autowired
    private AsyncEventBus eventBus;


    Queue<String> queue = new LinkedList<>();


    @Scheduled(initialDelay = 10000, fixedRate = 100)
    public void load() {
        gaugeService.submit("loader.link.queue-size", queueSize());
        if (readyToRun(commentLoaderJob.queueSize())) {

            StopWatch timer = new StopWatch();
            timer.start();

            ofNullable(queue.poll())
                    .flatMap(topicDisplayName -> process(topicDisplayName, 10))
                    .filter(links -> !links.isEmpty())
                    .ifPresent(links -> {
                        timer.stop();
                        linkManager.save(links);
                        log.debug(format("%20s: saved %3d links; took: %s", links.stream().findAny().get().getTopic(), links.size(), timer.toString()));
                        links.stream().forEach(eventBus::post);
                    });

            if (queue.isEmpty()) {
                log.info(format("Link queue is empty, utilise it more? Sleeping for %d seconds...", IDLE_SLEEP_IN_SECONDS));
                sleepUninterruptibly(IDLE_SLEEP_IN_SECONDS, SECONDS);
            }
        }
    }

    Optional<Collection<Link>> process(String topicDisplayName, int maxAttempts) {
        return rangeClosed(1, maxAttempts)
                .mapToObj(i -> {
                    try {
                        return load(submissions.ofSubreddit(topicDisplayName, HOT, -1, 100, null, null, true).stream(), linkConverter, linkValidator);
                    } catch (Exception ignore) {
                        log.info(format("Error retrieving links: iteration: %d, topic: %s. Sleeping for %d seconds then trying again", i, topicDisplayName, ERROR_SLEEP_IN_SECONDS));
                        sleepUninterruptibly(ERROR_SLEEP_IN_SECONDS, SECONDS);
                        return null;
                    }
                })
                .filter(links -> links != null)
                .findAny();
    }

    @Subscribe
    public void handle(Topic topic) {
        queue.add(topic.getDisplayName());
    }

    public int queueSize() {
        return queue.size();
    }
}