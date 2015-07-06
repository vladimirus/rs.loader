package rs.job;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.model.Link;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

@Service
public class LinkLoaderJob extends AbstractLoaderJob<Submission, Link> {
    @Autowired
    private Submissions submissions;
    @Autowired
    private Converter<Submission, Link> linkConverter;
    @Autowired
    private SimpleManager<Link> linkManager;

    @Scheduled(initialDelay = 10000, fixedRate = 5000)
    public void load() {
        load(submissions.ofSubreddit("funny", TOP, -1, 100, null, null, true).stream(), linkConverter, linkManager);
    }
}
