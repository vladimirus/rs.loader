package rs.loader.service.convert;

import org.springframework.stereotype.Component;
import rs.loader.model.Link;
import rs.loader.model.Suggestion;

@Component
public class SuggestionConverter implements Converter<Link, Suggestion> {

    @Override
    public Suggestion convert(Link link) {
        return Suggestion.builder()
                .id(String.valueOf(link.getTitle().hashCode()))
                .original(link.getTitle())
                .suggest(link.getTitle())
                .build();
    }
}
