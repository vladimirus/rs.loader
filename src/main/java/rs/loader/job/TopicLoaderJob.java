package rs.loader.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.rangeClosed;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
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

    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public synchronized void load() {
        if (readyToRun(linkLoaderJob.getQueueSize(), sleeping)) {
            sleeping = false;
            Optional<Collection<Topic>> optionalTopics = process(lastIndexedTopic(), 10);

            optionalTopics.ifPresent(topics -> {
                topicManager.save(topics);
                topics.stream().forEach(eventBus::post);
            });

            if (!optionalTopics.isPresent()) {
                process(Optional.<Topic>empty(), 5);
            }
        } else {
            sleeping = true;
        }
    }

    boolean readyToRun(int queueSize, boolean currentlySleeping) {
        return !(queueSize >= 5000 || (queueSize > 100 && currentlySleeping));
    }

    Optional<Collection<Topic>> process(Optional<Topic> startTopic, int maxAttempts) {
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
        Optional<Subreddit> subreddit = Optional.empty();

        if (lastCheckedTopic.isPresent()) {
            JSONObject json = new JSONObject();
            json.put("name", lastCheckedTopic.get().getId());
            json.put("created", 1.0);
            json.put("created_utc", 1.0);
            json.put("subscribers", 1L);
            subreddit = Optional.of(new Subreddit(json));
        }

        return subreddit;
    }

    private Optional<Topic> lastIndexedTopic() {
        return topicManager.get(0, 1)
                .stream()
                .findFirst();
    }
}
