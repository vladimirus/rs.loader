package rs.loader.service.convert;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static rs.loader.TestFactory.aLinkBuilder;
import static rs.loader.TestFactory.aSuggestion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.loader.dao.SuggestionDao;
import rs.loader.model.Suggestion;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class SuggestionConverterTest {
    @InjectMocks
    private SuggestionConverter suggestionConverter;
    @Mock
    private SuggestionDao suggestionDao;

    @Test
    public void shouldConvert() {
        // given
        given(suggestionDao.getById(anyString())).willReturn(singletonList(aSuggestion("123")));

        // when
        Collection<Suggestion> actual = suggestionConverter.convert(aLinkBuilder().title("this is test's one 2").build());

        // then
        assertThat(actual, hasSize(3));
    }

    @Test
    public void shouldConvertEmpty() {
        // given
        given(suggestionDao.getById(anyString())).willReturn(singletonList(aSuggestion("123")));

        // when
        Collection<Suggestion> actual = suggestionConverter.convert(aLinkBuilder().title("").build());

        // then
        assertThat(actual, emptyCollectionOf(Suggestion.class));
    }

    @Test
    public void shouldCleanse() {

        // when
        String actual = suggestionConverter.cleanse("This is, some TEXT 96; with extra ~ characters.");

        // then
        assertThat(actual, is("this is some text 96 with extra characters"));
    }
}