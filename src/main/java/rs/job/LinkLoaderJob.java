package rs.job;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.rangeClosed;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.model.Link;
import rs.model.Topic;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@Service
public class LinkLoaderJob extends AbstractLoaderJob<Submission, Link> {
    private Logger log = Logger.getLogger(LinkLoaderJob.class);
    @Autowired
    private Submissions submissions;
    @Autowired
    private Converter<Submission, Link> linkConverter;
    @Autowired
    private SimpleManager<Link> linkManager;
    @Autowired
    private SimpleManager<Topic> topicManager;
    @Autowired
    private GaugeService gaugeService;

    Queue<Topic> queue = new LinkedList<>();

    @Scheduled(initialDelay = 3000, fixedRate = 609999999) //once a week, or during start
    public void initQueue() {
        if (queue.isEmpty()) {
            Collection<Topic> topics = topicManager.get(0, 1000);
            if (topics != null && !topics.isEmpty()) {
                queue.addAll(topics);
            }
        }
    }

    @Scheduled(initialDelay = 10000, fixedRate = 100)
    public void load() {
        gaugeService.submit("loader.link.queue-size", getQueueSize());
        Optional<Topic> topicOptional = ofNullable(queue.poll());
        if(!topicOptional.isPresent()) {
            log.info("Link queue is empty, utilise it more?");
        }

        topicOptional.ifPresent(topic -> rangeClosed(1, 100)
                .mapToObj(i -> {
                    try {
                        return load(submissions.ofSubreddit(topic.getDisplayName(), TOP, -1, 100, null, null, true).stream(), linkConverter);
                    } catch (Exception ignore) {
                        log.info(String.format("Error retrieving links. Trying again, iteration: %d, topic: %s", i, topic.getDisplayName()));
                        sleepUninterruptibly(10, SECONDS);
                        return EMPTY_LIST;
                    }
                })
                .filter(links -> !links.isEmpty())
                .findAny()
                .ifPresent(linkManager::save));
    }

    @Subscribe
    public void handle(Topic topic) {
        queue.add(topic);
    }

    public int getQueueSize() {
        return queue.size();
    }
}