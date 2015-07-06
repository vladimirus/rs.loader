package rs.job;

import org.apache.log4j.Logger;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

import java.util.stream.Stream;

public abstract class AbstractLoaderJob<F, T> {
    private Logger log = Logger.getLogger(AbstractLoaderJob.class);

    void load(Stream<F> stream, Converter<F, T> converter, SimpleManager<T> manager) {
        stream.map(original -> {
                    try {
                        return converter.convert(original);
                    } catch (Exception e) {
                        log.error("can't convert, ignoring", e);
                        return null;
                    }
                })
                .filter(out -> out != null)
                .forEach(out -> {
                    try {
                        manager.save(out);
                    } catch (Exception e) {
                        log.error("can't save, ignoring", e);
                    }
                });
    }
}
