package rs.service.convert;

import com.github.jreddit.entity.Submission;
import org.springframework.stereotype.Component;
import rs.model.Link;

import java.util.Date;

@Component
public class LinkConverter implements Converter<Submission, Link> {

    @Override
    public Link convert(Submission submission) {
        return Link.builder()
                .id(submission.getFullName())
                .title(submission.getTitle())
                .url(submission.getUrl())
                .commentsUrl(submission.getPermalink())
                .author(submission.getAuthor())
                .subreddit(submission.getSubreddit())
                .subredditId(submission.getSubredditId())
                .commentCount(submission.getCommentCount())
                .score(submission.getScore())
                .created(new Date((long) submission.getCreatedUTC().doubleValue() * 1000))
                .self(submission.isSelf())
                .nsfw(submission.isNSFW())
                .hidden(submission.isHidden())
                .build();
    }
}
