package rs.loader.service.convert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.jreddit.entity.Subreddit;
import org.junit.Before;
import org.junit.Test;
import rs.loader.model.Topic;

public class TopicConverterTest {
    private TopicConverter topicConverter;

    @Before
    public void setup() {
        this.topicConverter = new TopicConverter();
    }

    @Test
    public void shouldConvert() {
        // given
        Subreddit submission = mock(Subreddit.class);
        given(submission.getFullName()).willReturn("name");
        given(submission.getDisplayName()).willReturn("display");
        given(submission.getTitle()).willReturn("title");
        given(submission.getCreatedUTC()).willReturn(1.0);
        given(submission.isNSFW()).willReturn(false);
        given(submission.getSubscribers()).willReturn(1L);
        given(submission.getDescription()).willReturn("this is description");
        given(submission.getSubredditType()).willReturn("public");

        // when
        Topic actual = topicConverter.convert(submission);

        // then
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getId(), is("name"));
    }
}