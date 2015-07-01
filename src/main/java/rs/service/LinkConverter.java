package rs.service;

import com.github.jreddit.entity.Submission;
import org.springframework.stereotype.Component;
import rs.model.Link;

@Component
public class LinkConverter {

    public Link convert(Submission submission) {
        return new Link(submission.getFullName(), submission.getTitle(), submission.getUrl());
    }
}
