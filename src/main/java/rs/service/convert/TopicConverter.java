package rs.service.convert;

import com.github.jreddit.entity.Subreddit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rs.model.Topic;

@Component
@Qualifier("topicConverter")
public class TopicConverter implements Converter<Subreddit, Topic> {

    @Override
    public Topic convert(Subreddit subreddit) {
        return new Topic(subreddit.getFullName());
    }
}
