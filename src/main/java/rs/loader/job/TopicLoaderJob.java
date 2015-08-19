package rs.loader.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.rangeClosed;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.loader.model.Topic;
import rs.loader.service.SimpleManager;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

import java.util.Collection;
import java.util.Optional;

@Service
public class TopicLoaderJob extends AbstractLoaderJob<Subreddit, Topic> {
    private Logger log = Logger.getLogger(TopicLoaderJob.class);
    @Autowired
    private Subreddits subreddits;
    @Autowired
    private Converter<Subreddit, Topic> topicConverter;
    @Autowired
    private Validator<Topic> topicValidator;
    @Autowired
    private SimpleManager<Topic> topicManager;
    @Autowired
    private LinkLoaderJob linkLoaderJob;
    @Autowired
    private AsyncEventBus eventBus;

    boolean sleeping;
    private Cache<String, Topic> cache = CacheBuilder.newBuilder().expireAfterWrite(1, HOURS).build();

    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public synchronized void load() {
        if (readyToRun(linkLoaderJob.getQueueSize(), sleeping)) {
            process(topicToCheck(lastIndexedTopics()), 10).ifPresent(
                    retrievedTopics -> {
                        topicManager.save(retrievedTopics);
                        retrievedTopics.stream().forEach(eventBus::post);
                    }
            );
            sleeping = false;
        } else {
            sleeping = true;
        }
    }

    boolean readyToRun(int queueSize, boolean currentlySleeping) {
        return !(queueSize >= 5000 || (queueSize > 100 && currentlySleeping));
    }

    Optional<Collection<Topic>> process(Optional<Topic> startTopic, int maxAttempts) {
        startTopic.ifPresent(topic -> {
            log.info(format("Retrieving topics, start-topic-name: %s (%s)", topic.getDisplayName(), topic.getId()));
        });

        return rangeClosed(1, maxAttempts)
                .mapToObj(i -> {
                    try {
                        return load(subreddits.get(POPULAR, 0, 100, lastSubreddit(startTopic).orElse(null), null).stream(), topicConverter, topicValidator);
                    } catch (Exception ignore) {
                        log.info(format("Error retrieving topics. Trying again, iteration: %d, start-topic: %s", i, startTopic.isPresent() ? startTopic.get().getDisplayName() : "<null>"));
                        sleepUninterruptibly(2, SECONDS);
                        return null;
                    }
                })
                .filter(topics -> topics != null)
                .findAny();
    }

    @SuppressWarnings("unchecked")
    Optional<Subreddit> lastSubreddit(Optional<Topic> lastCheckedTopic) {
        return lastCheckedTopic.map(topic -> {
            JSONObject json = new JSONObject();
            json.put("name", lastCheckedTopic.get().getId());
            json.put("created", 1.0);
            json.put("created_utc", 1.0);
            json.put("subscribers", 1L);
            return new Subreddit(json);
        });
    }

    private Optional<Topic> topicToCheck(Collection<Topic> recentTopics) {
        return recentTopics.stream()
                .filter(topic -> cache.getIfPresent(topic.getId()) == null)
                .peek(topic -> cache.put(topic.getId(), topic))
                .findAny();
    }

    private Collection<Topic> lastIndexedTopics() {
        return topicManager.get(0, 100);
    }
}
