package rs.loader.job;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import rs.loader.service.convert.Converter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

public abstract class AbstractLoaderJob<F, T> {
    private Logger log = Logger.getLogger(AbstractLoaderJob.class);

    private LocalDateTime lastProcessed = now();
    @Autowired
    private CounterService counterService;

    Collection<T> load(Stream<F> stream, Converter<F, T> converter) {
        return stream.map(original -> {
                    try {
                        return converter.convert(original);
                    } catch (Exception e) {
                        log.error("can't convert, ignoring", e);
                        return null;
                    }
                })
                .peek(out -> lastProcessed = now())
                .filter(out -> out != null)
                .peek(out -> counterService.increment("loader." + out.getClass().getSimpleName().toLowerCase()))
                .collect(toList());
    }

    public LocalDateTime getLastProcessed() {
        return lastProcessed;
    }
}