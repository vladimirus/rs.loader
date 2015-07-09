package rs.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;
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

import java.util.Collection;

@Service
public class TopicLoaderJob extends AbstractLoaderJob<Subreddit, Topic> {
    @Autowired
    private Subreddits subreddits;
    @Autowired
    private Converter<Subreddit, Topic> topicConverter;
    @Autowired
    private SimpleManager<Topic> topicManager;

    Topic last;
    int numberOrErrorsInARow;

    @Scheduled(initialDelay = 100, fixedRate = 609999999) //once a week, or during start
    public void initLast() {
        if (last == null) {
            Collection<Topic> topics = topicManager.get(0, 1);
            if (topics != null && !topics.isEmpty()) {
                last = topics.iterator().next();
            }
        }
    }

    @Scheduled(initialDelay = 5000, fixedRate = 10000)
    public void load() {
        try {
            last = getLast(load(subreddits.get(POPULAR, 0, 100, numberOrErrorsInARow < 10 ? lastSubreddit(last) : null, null).stream(), topicConverter, topicManager));
            numberOrErrorsInARow = 0;
        } catch (Exception e) {
            log.error("Error retrieving subreddits", e);
            numberOrErrorsInARow++;
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
}
