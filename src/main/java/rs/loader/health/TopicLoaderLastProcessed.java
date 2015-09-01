package rs.loader.health;

import static java.time.LocalDateTime.now;
import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rs.loader.job.TopicLoaderJob;

import java.time.LocalDateTime;

@Component
public class TopicLoaderLastProcessed implements HealthIndicator {
    @Autowired
    private TopicLoaderJob topicLoaderJob;

    @Override
    public Health health() {
        LocalDateTime lastProcessed = topicLoaderJob.getLastProcessed();
        if (lastProcessed.isBefore(now().minusHours(6))) {
            return down().withDetail("topic.last-processed", lastProcessed.toString()).build();
        }
        return up().withDetail("topic.last-processed", lastProcessed.toString()).build();
    }

}
