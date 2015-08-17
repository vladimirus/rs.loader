package rs.loader.health;

import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rs.loader.job.LinkLoaderJob;

import java.time.LocalDateTime;

@Component
public class LinkLoaderLastProcessed implements HealthIndicator {
    @Autowired
    private LinkLoaderJob linkLoaderJob;

    @Override
    public Health health() {
        LocalDateTime lastProcessed = linkLoaderJob.getLastProcessed();
        if (lastProcessed.isBefore(LocalDateTime.now().minusMinutes(10))) {
            return down().withDetail("link.last-processed", lastProcessed.toString()).build();
        }
        return up().withDetail("link.last-processed", lastProcessed.toString()).build();
    }

}
