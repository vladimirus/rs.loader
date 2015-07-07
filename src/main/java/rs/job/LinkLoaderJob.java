package rs.job;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.model.Link;
import rs.model.Topic;
import rs.service.convert.Converter;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class LinkLoaderJob extends AbstractLoaderJob<Submission, Link> {
    @Autowired
    private Submissions submissions;
    @Autowired
    private Converter<Submission, Link> linkConverter;

    Queue<Topic> queue = new LinkedList<>();

    @Scheduled(initialDelay = 10000, fixedRate = 5000)
    public void load() {
        Topic topic = queue.poll();
        if (topic != null) {
            load(submissions.ofSubreddit(topic.getDisplayName(), TOP, -1, 100, null, null, true).stream(), linkConverter);
        }
    }

    @Subscribe
    public void handle(Topic topic) {
        queue.add(topic);
    }
}
