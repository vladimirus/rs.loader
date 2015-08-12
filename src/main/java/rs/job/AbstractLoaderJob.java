package rs.job;

import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import rs.service.convert.Converter;

import java.util.Collection;
import java.util.stream.Stream;

public abstract class AbstractLoaderJob<F, T> {
    Logger log = Logger.getLogger(AbstractLoaderJob.class);
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
                .filter(out -> out != null)
                .peek(out -> counterService.increment("loader." + out.getClass().getSimpleName().toLowerCase()))
                .collect(toList());
    }
}
