package rs.loader.service.convert;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.stream.Collectors.toList;

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

@Component
public class SuggestionConverter {
    @Autowired
    private SuggestionDao suggestionDao;

    private Cache<String, AtomicInteger> cache = CacheBuilder.newBuilder().expireAfterWrite(10, HOURS).build();

    private Collection<String> startsWith = asList("Who", "What", "Why", "When", "Where", "How");

    public Collection<Suggestion> convert(Link link) {
        Collection<Suggestion> result = split(link.getTitle());

        of(link.getTitle())
                .filter(this::matches)
                .map(t -> t.trim().replaceAll(" .$", ""))
                .ifPresent(text -> result.add(create(text, text, 100)));

        return result;
    }

    private boolean matches(String text) {
        return of(text)
                .filter(t -> t.length() > 10)
                .filter(t -> t.length() < 50)
                .filter(this::startsWith)
                .isPresent();
    }

    private boolean startsWith(String t) {
        return startsWith.stream()
                .filter(t::startsWith)
                .findAny()
                .isPresent();
    }

    private Collection<Suggestion> split(String text) {
        return Splitter.on(' ')
                .omitEmptyStrings()
                .trimResults()
                .splitToList(cleanse(text)).stream()
                .filter(s -> s.length() > 2)
                .map(s -> create(text, s, s.length()))
                .collect(toList());
    }

    private Suggestion create(String text, String part, Integer boost) {
        return Suggestion.builder()
                .id(String.valueOf(part))
                .original(text)
                .suggest(Suggest.builder()
                        .input(part)
                        .output(part)
                        .weight(weight(part) * boost)
                        .build())
                .build();
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
