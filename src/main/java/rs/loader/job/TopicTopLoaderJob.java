package rs.loader.job;

import com.google.common.eventbus.AsyncEventBus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.loader.service.TopicManager;

@Service
public class TopicTopLoaderJob {
    static int SIZE_OF_TOPICS_TO_COLLECT = 1000;
    private Logger log = Logger.getLogger(TopicTopLoaderJob.class);
    @Autowired
    private TopicManager topicManager;
    @Autowired
    private LinkLoaderJob linkLoaderJob;
    @Autowired
    private AsyncEventBus eventBus;

    @Value("${rs.loader.topic.top.enabled:false}")
    boolean enabled;

    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public void load() {
        if (enabled && linkLoaderJob.queueSize() <= 10) {
            log.debug("Staring TopicTopLoaderJob");
            topicManager.getTop(SIZE_OF_TOPICS_TO_COLLECT).stream().forEach(eventBus::post);
        }
    }
}
