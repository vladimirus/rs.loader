package rs.health;

import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rs.job.TopicLoaderJob;

import java.time.LocalDateTime;

@Component
public class TopicLoaderLastProcessed implements HealthIndicator {
    @Autowired
    private TopicLoaderJob topicLoaderJob;

    @Override
    public Health health() {
        LocalDateTime lastProcessed = topicLoaderJob.getLastProcessed();
        if (lastProcessed.isBefore(LocalDateTime.now().minusMinutes(30))) {
            return down().withDetail("link.last-processed", lastProcessed.toString()).build();
        }
        return up().withDetail("link.last-processed", lastProcessed.toString()).build();
    }

}
