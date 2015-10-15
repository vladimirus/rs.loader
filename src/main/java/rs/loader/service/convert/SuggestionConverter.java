package rs.loader.service.convert;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.HOURS;

import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.loader.dao.SuggestionDao;
import rs.loader.model.Link;
import rs.loader.model.Suggest;
import rs.loader.model.Suggestion;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class SuggestionConverter {
    @Autowired
    private SuggestionDao suggestionDao;

    private Cache<String, AtomicInteger> cache = CacheBuilder.newBuilder().expireAfterWrite(10, HOURS).build();

    public Collection<Suggestion> convert(Link link) {
        return Splitter.on(' ')
                .omitEmptyStrings()
                .trimResults()
                .splitToList(cleanse(link.getTitle())).stream()
                .filter(s -> s.length() > 2)
                .map(s -> Suggestion.builder()
                        .id(String.valueOf(s))
                        .original(link.getTitle())
                        .suggest(Suggest.builder()
                                .input(s)
                                .output(s)
                                .weight(weight(s))
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    String cleanse(String text) {
        return ofNullable(text).orElse("")
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll(" +", " ")
                .trim();
    }

    @SneakyThrows
    private Integer weight(String s) {
        return cache.get(s, () -> retrieveSuggestion(s)).incrementAndGet();
    }

    private AtomicInteger retrieveSuggestion(String s) {
        return new AtomicInteger(suggestionDao.getById(s).stream()
                .map(suggest -> suggest.getSuggest().getWeight())
                .findAny().orElse(0));
    }
}
