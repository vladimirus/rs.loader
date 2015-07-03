package rs.job;

import static com.github.jreddit.retrieval.params.SubredditsView.POPULAR;

import com.github.jreddit.retrieval.Subreddits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TopicLoaderJob {
    @Autowired
    private Subreddits subreddits;

    @Scheduled(initialDelay = 5000, fixedRate = 120000)
    public void load() {
        subreddits.get(POPULAR, 0, 100, null, null);
    }
}
