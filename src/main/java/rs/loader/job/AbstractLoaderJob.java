package rs.loader.job;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

public abstract class AbstractLoaderJob<F, T> {
    private Logger log = Logger.getLogger(AbstractLoaderJob.class);

    private LocalDateTime lastProcessed = now();
    @Autowired
    private CounterService counterService;

    Collection<T> load(Stream<F> stream, Converter<F, T> converter, Validator<T> validator) {
        return stream.map(original -> {
                    try {
                        return converter.convert(original);
                    } catch (Exception e) {
                        log.error("can't convert, ignoring", e);
                        return null;
                    }
                })
                .filter(out -> out != null)
                .peek(out -> lastProcessed = now())
                .peek(out -> counterService.increment(format("loader.%s.step01.notNull", out.getClass().getSimpleName().toLowerCase())))
                .filter(validator::isValid)
                .peek(out -> counterService.increment(format("loader.%s.step02.filtered", out.getClass().getSimpleName().toLowerCase())))
                .collect(toList());
    }

    public LocalDateTime getLastProcessed() {
        return lastProcessed;
    }
}
