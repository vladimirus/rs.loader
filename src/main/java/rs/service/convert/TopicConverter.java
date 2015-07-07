package rs.service.convert;

import com.github.jreddit.entity.Subreddit;
import org.springframework.stereotype.Component;
import rs.model.Topic;

@Component
public class TopicConverter implements Converter<Subreddit, Topic> {

    @Override
    public Topic convert(Subreddit subreddit) {
        return Topic.builder()
                .id(subreddit.getFullName())
                .build();
    }
}
