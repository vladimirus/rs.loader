package rs.service.convert;

import com.github.jreddit.entity.Submission;
import org.springframework.stereotype.Component;
import rs.model.Link;

@Component
public class LinkConverter implements Converter<Submission, Link> {

    @Override
    public Link convert(Submission submission) {
        return Link.builder()
                .id(submission.getFullName())
                .title(submission.getTitle())
                .url(submission.getUrl())
                .build();
    }
}
