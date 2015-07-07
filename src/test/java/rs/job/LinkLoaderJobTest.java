package rs.job;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static rs.TestFactory.aLink;
import static rs.TestFactory.aTopic;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import com.google.common.eventbus.AsyncEventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.model.Link;
import rs.model.Topic;
import rs.service.convert.Converter;

@RunWith(MockitoJUnitRunner.class)
public class LinkLoaderJobTest {
    @InjectMocks
    private LinkLoaderJob linkLoaderJob;
    @Mock
    private Submissions submissions;
    @Mock
    private Converter<Submission, Link> linkConverter;
    @Mock
    private AsyncEventBus eventBus;

    @Test
    public void shouldNotLoadWhenNoTopics() {

        // when
        linkLoaderJob.load();

        // then
        verify(linkConverter, never()).convert(any(Submission.class));
        verify(eventBus, never()).post(any(Link.class));
    }

    @Test
    public void shouldLoad() {
        // given
        linkLoaderJob.queue.add(aTopic());
        Submission submission = mock(Submission.class);
        given(submissions.ofSubreddit(anyString(), any(SubmissionSort.class), eq(-1), eq(100), eq(null), eq(null), eq(true)))
                .willReturn(asList(submission, submission));
        given(linkConverter.convert(any(Submission.class))).willReturn(aLink());

        // when
        linkLoaderJob.load();

        // then
        verify(linkConverter, times(2)).convert(any(Submission.class));
        verify(eventBus, times(2)).post(any(Link.class));
    }

    @Test
    public void shouldLoadTwo() {
        // given
        linkLoaderJob.queue.add(aTopic());
        Submission submission = mock(Submission.class);
        given(submissions.ofSubreddit(anyString(), any(SubmissionSort.class), eq(-1), eq(100), eq(null), eq(null), eq(true)))
                .willReturn(asList(submission, submission, submission));
        given(linkConverter.convert(any(Submission.class)))
                .willReturn(aLink())
                .willThrow(new RuntimeException("test"))
                .willReturn(aLink());

        // when
        linkLoaderJob.load();

        // then
        verify(linkConverter, times(3)).convert(any(Submission.class));
        verify(eventBus, times(2)).post(any(Link.class));
    }

    @Test
    public void shouldHandleTopic() {
        // given
        Topic topic = aTopic("id123");

        // when
        linkLoaderJob.handle(topic);

        // then
        assertThat(linkLoaderJob.queue, contains(topic));
    }
}