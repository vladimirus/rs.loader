package rs.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
import com.google.common.eventbus.AsyncEventBus;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.model.Topic;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

import java.util.Collection;

@Service
public class TopicLoaderJob extends AbstractLoaderJob<Subreddit, Topic> {
    @Autowired
    private Subreddits subreddits;
    @Autowired
    private Converter<Subreddit, Topic> topicConverter;
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
            process(lastIndexedTopic(), 0, 100);
        } else {
            sleeping = true;
        }
    }

    boolean readyToRun(int queueSize, boolean currentlySleeping) {
        return !(queueSize >= 10000 || (queueSize > 100 && currentlySleeping));
    }

    void process(Topic startTopic, int attemptsMade, int maxAttempts) {
        while(attemptsMade < maxAttempts) {
            if (attemptsMade > maxAttempts - 10) {   //after 90 attempts, we couldn't get subreddit, so start from 0
                startTopic = null;
            }
            try {
                Collection<Topic> topics = load(subreddits.get(POPULAR, 0, 100, lastSubreddit(startTopic), null).stream(), topicConverter);
                topicManager.save(topics);
                topics.stream().forEach(eventBus::post);
                break;
            } catch (Exception ignore) {
                sleepUninterruptibly(1, SECONDS);
                attemptsMade++;
            }
        }
    }

    @SuppressWarnings("unchecked")
    Subreddit lastSubreddit(Topic lastCheckedTopic) {
        Subreddit subreddit = null;

        if (lastCheckedTopic != null) {
            JSONObject json = new JSONObject();
            json.put("name", lastCheckedTopic.getId());
            json.put("created", 1.0);
            json.put("created_utc", 1.0);
            json.put("subscribers", 1L);
            subreddit = new Subreddit(json);
        }

        return subreddit;
    }

    private Topic lastIndexedTopic() {
        return topicManager.get(0, 1)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
