package rs.health;

import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rs.job.LinkLoaderJob;

@Component
public class LinkLoaderQueueSize implements HealthIndicator {
    @Autowired
    private LinkLoaderJob linkLoaderJob;

    @Override
    public Health health() {
        int queueSize = linkLoaderJob.getQueueSize();
        if (queueSize < 10) {
            return down().withDetail("queue-size", queueSize).build();
        }
        return up().withDetail("queue-size", queueSize).build();
    }

}
