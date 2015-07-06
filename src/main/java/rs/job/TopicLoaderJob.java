package rs.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.model.Topic;
import rs.service.convert.Converter;

@Service
public class TopicLoaderJob {
    @Autowired
    private Subreddits subreddits;
    @Autowired
    @Qualifier("topicConverter")
    private Converter<Subreddit, Topic> topicConverter;

    @Scheduled(initialDelay = 5000, fixedRate = 120000)
    public void load() {
        subreddits.get(POPULAR, 0, 100, null, null)
                .stream()
                .map(subreddit -> topicConverter.convert(subreddit))
                .forEach(topic -> {

                });
    }
}
