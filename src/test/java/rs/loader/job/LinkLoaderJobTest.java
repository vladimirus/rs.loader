package rs.loader.job;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import rs.loader.model.Link;
import rs.loader.model.Topic;
import rs.loader.service.SimpleManager;
import rs.loader.service.convert.Converter;

@RunWith(MockitoJUnitRunner.class)
public class LinkLoaderJobTest {
    @InjectMocks
    private LinkLoaderJob linkLoaderJob;
    @Mock
    private Submissions submissions;
    @Mock
    private Converter<Submission, Link> linkConverter;
    @Mock
    private SimpleManager<Link> linkManager;
    @Mock
    private SimpleManager<Topic> topicManager;
    @Mock
    private CounterService counterService;
    @Mock
    private GaugeService gaugeService;
    @Test
    public void shouldNotLoadWhenNoTopics() {

        // when
        linkLoaderJob.load();

        // then
        verify(linkConverter, never()).convert(any(Submission.class));
        verify(linkManager, never()).save(any(Link.class));
    }

    @SuppressWarnings("unchecked")
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
        verify(linkManager).save(anyCollectionOf(Link.class));
        verify(gaugeService).submit(anyString(), anyDouble());
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
        verify(linkManager).save(anyCollectionOf(Link.class));
        verify(counterService, times(2)).increment(anyString());
        verify(gaugeService).submit(anyString(), anyDouble());
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

    @Test
    public void shouldInitQueue() {
        // given
        given(topicManager.get(isA(Integer.class), isA(Integer.class))).willReturn(singletonList(aTopic()));

        // when
        linkLoaderJob.initQueue();

        // then
        verify(topicManager).get(isA(Integer.class), isA(Integer.class));
    }

    @Test
    public void shouldNotInitQueue() {
        // given
        linkLoaderJob.queue.add(aTopic());

        // when
        linkLoaderJob.initQueue();

        // then
        verify(topicManager, never()).get(isA(Integer.class), isA(Integer.class));
    }
}
