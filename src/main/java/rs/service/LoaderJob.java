package rs.service;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.utils.restclient.RestClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LoaderJob {
    private Logger log = Logger.getLogger(LoaderJob.class);

    @Autowired
    private RestClient redditClient;
    @Autowired
    private User redditUser;


    @Scheduled(fixedRate = 5000)
    public void load() {
        log.debug("yo!");

        Submissions submissions = new Submissions(redditClient, redditUser);

        Collection<Submission> list = submissions.ofSubreddit("funny", TOP, -1, 100, null, null, true);

        list.forEach(submission -> System.out.println(submission.getTitle()));
    }
}
