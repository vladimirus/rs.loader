package rs.job;

import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

import java.util.Collection;
import java.util.stream.Stream;

public abstract class AbstractLoaderJob<F, T> {
    Logger log = Logger.getLogger(AbstractLoaderJob.class);

    Collection<T> load(Stream<F> stream, Converter<F, T> converter, SimpleManager<T> manager) {
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
                        manager.save(out);
                        return true;
                    } catch (Exception e) {
                        log.error("can't save, ignoring", e);
                        return false;
                    }
                })
                .collect(toList());
    }
}
