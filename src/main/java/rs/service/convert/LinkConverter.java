package rs.service.convert;

import com.github.jreddit.entity.Submission;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rs.model.Link;

@Component
@Qualifier("linkConverter")
public class LinkConverter implements Converter<Submission, Link> {

    @Override
    public Link convert(Submission submission) {
        return new Link(submission.getFullName(), submission.getTitle(), submission.getUrl());
    }
}
