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
    public void shouldConvert() {
        // given
        Submission submission = mock(Submission.class);
        given(submission.getFullName()).willReturn("id");
        given(submission.getTitle()).willReturn("title");
        given(submission.getUrl()).willReturn("url");
        given(submission.getPermalink()).willReturn("commentsUrl");
        given(submission.getAuthor()).willReturn("author");
        given(submission.getSubreddit()).willReturn("subreddit");
        given(submission.getSubredditId()).willReturn("subredditId");
        given(submission.getCommentCount()).willReturn(1L);
        given(submission.getScore()).willReturn(1L);
        given(submission.getCreatedUTC()).willReturn(1.0);
        given(submission.isSelf()).willReturn(false);
        given(submission.isNSFW()).willReturn(false);
        given(submission.isHidden()).willReturn(false);

        // when
        Link actual = linkConverter.convert(submission);

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getTitle(), is("title"));
        assertThat(actual.getUrl(), is("url"));
    }
}