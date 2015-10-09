package rs.loader.service.convert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static rs.loader.TestFactory.aLinkBuilder;

import org.junit.Before;
import org.junit.Test;
import rs.loader.model.Suggestion;

public class SuggestionConverterTest {
    private SuggestionConverter suggestionConverter;

    @Before
    public void setup() {
        this.suggestionConverter = new SuggestionConverter();
    }

    @Test
    public void shouldConvert() {

        // when
        Suggestion actual = suggestionConverter.convert(aLinkBuilder().title("title").build());

        // then
        assertThat(actual.getOriginal(), is("title"));
        assertThat(actual.getId(), is(String.valueOf("title".hashCode())));
    }
}