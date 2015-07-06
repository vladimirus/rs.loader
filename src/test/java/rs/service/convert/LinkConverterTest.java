package rs.service.convert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.jreddit.entity.Submission;
import org.junit.Before;
import org.junit.Test;
import rs.model.Link;

public class LinkConverterTest {
    private LinkConverter linkConverter;

    @Before
    public void setup() {
        this.linkConverter = new LinkConverter();
    }

    @Test
    public void shouldConvert() throws Exception {
        // given
        Submission submission = mock(Submission.class);
        given(submission.getTitle()).willReturn("title");
        given(submission.getUrl()).willReturn("url");
        given(submission.getFullName()).willReturn("name");

        // when
        Link actual = linkConverter.convert(submission);

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getTitle(), is("title"));
        assertThat(actual.getUrl(), is("url"));
    }
}