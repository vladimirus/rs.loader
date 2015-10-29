package rs.loader.job;

import com.google.common.eventbus.AsyncEventBus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.loader.service.LinkManager;

@Service
public class CommentMissingLoaderJob {
    private Logger log = Logger.getLogger(CommentMissingLoaderJob.class);
    @Autowired
    private CommentLoaderJob commentLoaderJob;
    @Autowired
    private LinkManager linkManager;
    @Value("${rs.loader.comment.missing.enabled:true}")
    boolean enabled;
    @Autowired
    private AsyncEventBus eventBus;

    @Scheduled(initialDelay = 100000, fixedRate = 10000)
    public void load() {
        if (enabled && commentLoaderJob.queueSize() < 50) {
            log.debug("Staring CommentMissingLoaderJob");
            linkManager.getMissingComments(0, 100).stream().forEach(eventBus::post);
        }
    }
}
