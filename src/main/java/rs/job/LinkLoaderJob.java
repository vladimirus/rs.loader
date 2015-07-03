package rs.job;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;

import com.github.jreddit.retrieval.Submissions;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.service.LinkConverter;
import rs.service.LinkManager;

@Service
public class LinkLoaderJob {
    private Logger log = Logger.getLogger(LinkLoaderJob.class);
    @Autowired
    private Submissions submissions;
    @Autowired
    private LinkConverter linkConverter;
    @Autowired
    private LinkManager linkManager;

    @Scheduled(initialDelay = 10000, fixedRate = 5000)
    public void load() {
        submissions.ofSubreddit("funny", TOP, -1, 25, null, null, true)
                .forEach(submission -> linkManager.save(linkConverter.convert(submission)));
    }
}
