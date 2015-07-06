package rs.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
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

    @Scheduled(initialDelay = 5000, fixedRate = 120000)
    public void load() {
        load(subreddits.get(POPULAR, 0, 100, null, null).stream(), topicConverter, topicManager);
    }
}
