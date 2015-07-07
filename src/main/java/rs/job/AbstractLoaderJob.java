package rs.job;

import com.google.common.eventbus.AsyncEventBus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import rs.service.convert.Converter;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractLoaderJob<F, T> {
    private Logger log = Logger.getLogger(AbstractLoaderJob.class);
    @Autowired
    private AsyncEventBus eventBus;

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
                .filter(out -> {
                    try {
                        eventBus.post(out);
                        return true;
                    } catch (Exception e) {
                        log.error("can't post, ignoring", e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
