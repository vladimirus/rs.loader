package rs.loader.health;

import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rs.loader.job.CommentLoaderJob;

@Component
public class CommentLoaderQueueSize implements HealthIndicator {
    @Autowired
    private CommentLoaderJob commentLoaderJob;

    @Override
    public Health health() {
        int queueSize = commentLoaderJob.queueSize();
        if (queueSize < 10) {
            return down().withDetail("queue-size", queueSize).build();
        }
        return up().withDetail("queue-size", queueSize).build();
    }

}
