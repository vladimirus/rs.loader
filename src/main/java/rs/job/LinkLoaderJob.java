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
import rs.service.SimpleManager;
import rs.service.convert.Converter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class LinkLoaderJob extends AbstractLoaderJob<Submission, Link> {
    @Autowired
    private Submissions submissions;
    @Autowired
    private Converter<Submission, Link> linkConverter;
    @Autowired
    private SimpleManager<Link> linkManager;
    @Autowired
    private SimpleManager<Topic> topicManager;

    Queue<Topic> queue = new LinkedList<>();

    @Scheduled(initialDelay = 500, fixedRate = 609999999) //once a week, or during start
    public void initQueue() {
        if (queue.isEmpty()) {
            Collection<Topic> topics = topicManager.get(0, 1000);
            if (topics != null && !topics.isEmpty()) {
                queue.addAll(topics);
            }
        }
    }

    @Scheduled(initialDelay = 10000, fixedRate = 100)
    public void load() {
        Topic topic = queue.poll();
        if (topic != null) {
            load(submissions.ofSubreddit(topic.getDisplayName(), TOP, -1, 100, null, null, true).stream(), linkConverter, linkManager);
        }
    }

    @Subscribe
    public void handle(Topic topic) {
        queue.add(topic);
    }
}
