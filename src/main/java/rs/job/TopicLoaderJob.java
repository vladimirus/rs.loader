package rs.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.elasticsearch.common.collect.Iterables.getLast;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.model.Topic;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

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

    boolean sleeping;

    @Scheduled(initialDelay = 5000, fixedRate = 60000)
    public synchronized void load() {
        if (readyToRun(linkLoaderJob.getQueueSize(), sleeping)) {
            sleeping = false;
            process(lastIndexedTopic(), 0, 100);
        } else {
            sleeping = true;
        }
    }

    boolean readyToRun(int queueSize, boolean currentlySleeping) {
        return !(queueSize >= 10000 || (queueSize > 10 && currentlySleeping));
    }

    Topic process(Topic startTopic, int attemptsMade, int maxAttempts) {
        while(attemptsMade < maxAttempts) {
            if (attemptsMade > maxAttempts - 10) {   //after 90 attempts, we couldn't get subreddit, so start from 0
                startTopic = null;
            }
            try {
                return getLast(load(subreddits.get(POPULAR, 0, 100, lastSubreddit(startTopic), null).stream(), topicConverter, topicManager));
            } catch (Exception ignore) {
                sleepUninterruptibly(1, SECONDS);
                attemptsMade++;
            }
        }
        return null;
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
