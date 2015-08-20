package rs.loader.job;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static rs.loader.TestFactory.aLink;
import static rs.loader.TestFactory.aTopic;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import org.junit.Before;
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
import rs.loader.service.validator.Validator;

@RunWith(MockitoJUnitRunner.class)
public class LinkLoaderJobTest {
    @InjectMocks
    private LinkLoaderJob linkLoaderJob;
    @Mock
    private Submissions submissions;
    @Mock
    private Converter<Submission, Link> linkConverter;
    @Mock
    private Validator<Link> linkValidator;
    @Mock
    private SimpleManager<Link> linkManager;
    @Mock
    private CounterService counterService;
    @Mock
    private GaugeService gaugeService;

    @Before
    public void setup() {
        LinkLoaderJob.IDLE_SLEEP_IN_SECONDS = 0;
        LinkLoaderJob.ERROR_SLEEP_IN_SECONDS = 0;
    }

    @Test
    public void shouldNotLoadWhenNoTopics() {
        // given
        given(linkValidator.isValid(any(Link.class))).willReturn(true);

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
        given(linkValidator.isValid(any(Link.class))).willReturn(true);
        linkLoaderJob.queue.add("topic");
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
        given(linkValidator.isValid(any(Link.class))).willReturn(true);
        linkLoaderJob.queue.add("topic");
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
        verify(counterService, times(4)).increment(anyString());
        verify(gaugeService).submit(anyString(), anyDouble());
    }

    @Test
    public void shouldHandleTopic() {
        // given
        Topic topic = aTopic("id", "displayName");

        // when
        linkLoaderJob.handle(topic);

        // then
        assertThat(linkLoaderJob.queue, contains("displayName"));
    }
}
