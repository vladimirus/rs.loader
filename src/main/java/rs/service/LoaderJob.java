package rs.service;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;

import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.utils.restclient.RestClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.dao.LinkDao;

@Service
public class LoaderJob {
    private Logger log = Logger.getLogger(LoaderJob.class);

    @Autowired
    private RestClient redditClient;
    @Autowired
    private User redditUser;
    @Autowired
    private LinkConverter linkConverter;
    @Autowired
    private LinkDao linkDao;

    @Scheduled(fixedRate = 5000)
    public void load() {
        Submissions submissions = new Submissions(redditClient, redditUser);
        submissions.ofSubreddit("funny", TOP, -1, 100, null, null, true)
                .forEach(submission -> linkDao.save(linkConverter.convert(submission)));

    }
}
