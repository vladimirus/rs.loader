package rs.loader;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
@EnableAsync
public class RsLoaderConfigScheduling implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return newScheduledThreadPool(100);
    }

    @Bean(destroyMethod = "shutdown")
    @Qualifier(value = "commentExecutor")
    public Executor commentExecutor() {
        return newScheduledThreadPool(100);
    }

    @Bean(destroyMethod = "shutdown")
    @Qualifier(value = "linkExecutor")
    public Executor linkExecutor() {
        return newScheduledThreadPool(100);
    }
}
