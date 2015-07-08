package rs.service.convert;

import static java.util.Optional.ofNullable;

import com.github.jreddit.entity.Subreddit;
import org.springframework.stereotype.Component;
import rs.model.Topic;

import java.util.Date;

@Component
public class TopicConverter implements Converter<Subreddit, Topic> {

    @Override
    public Topic convert(Subreddit subreddit) {
        return Topic.builder()
                .id(subreddit.getFullName())
                .displayName(subreddit.getDisplayName())
                .title(subreddit.getTitle())
                .created(new Date((long) subreddit.getCreatedUTC() * 1000))
                .updated(new Date())
                .nsfw(subreddit.isNSFW())
                .subscribers(subreddit.getSubscribers())
                .description(ofNullable(subreddit.getDescription()).orElse(""))
                .type(subreddit.getSubredditType())
                .build();
    }
}
