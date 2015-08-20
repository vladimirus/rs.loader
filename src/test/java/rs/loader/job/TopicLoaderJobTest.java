package rs.loader.job;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static rs.loader.TestFactory.aTopic;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
import com.github.jreddit.retrieval.params.SubredditsView;
import com.google.common.eventbus.AsyncEventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.metrics.CounterService;
import rs.loader.model.Topic;
import rs.loader.service.SimpleManager;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

import java.util.Collection;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TopicLoaderJobTest {
    @InjectMocks
    private TopicLoaderJob topicLoaderJob;
    @Mock
    private Subreddits subreddits;
    @Mock
    private Converter<Subreddit, Topic> topicConverter;
    @Mock
    private Validator<Topic> topicValidator;
    @Mock
    private SimpleManager<Topic> topicManager;
    @Mock
    private LinkLoaderJob linkLoaderJob;
    @Mock
    private AsyncEventBus eventBus;
    @Mock
    private CounterService counterService;

    @Before
    public void setup() {
        TopicLoaderJob.SIZE_OF_TOPICS_TO_COLLECT = 5;
    }

    @Test
    public void shouldLoad() {
        // given
        Subreddit subreddit = mock(Subreddit.class);
        given(topicValidator.isValid(any(Topic.class))).willReturn(true);
        given(subreddits.get(any(SubredditsView.class), eq(0), eq(100), eq(null), eq(null))).willReturn(singletonList(subreddit));
        given(topicConverter.convert(any(Subreddit.class))).willReturn(aTopic());
        given(linkLoaderJob.getQueueSize()).willReturn(0, 1, 2);

        // when
        topicLoaderJob.load();

        // then
        verify(topicConverter, times(TopicLoaderJob.SIZE_OF_TOPICS_TO_COLLECT)).convert(any(Subreddit.class));
        verify(topicManager).save(anyCollectionOf(Topic.class));
        verify(eventBus, times(TopicLoaderJob.SIZE_OF_TOPICS_TO_COLLECT)).post(isA(Topic.class));
        verify(counterService, times(TopicLoaderJob.SIZE_OF_TOPICS_TO_COLLECT * 2)).increment(anyString());
    }

    @Test
    public void shouldGetLast() {

        // when
        Optional<Subreddit> actual = topicLoaderJob.lastSubreddit(Optional.of(aTopic("t5_2qh33")));

        // then
        assertThat(actual.get().getFullName(), is("t5_2qh33"));
    }

    @Test
    public void shouldNotGetLast() {

        // when
        Optional<Subreddit> actual = topicLoaderJob.lastSubreddit(Optional.<Topic>empty());

        // then
        assertThat(actual.isPresent(), is(false));
    }

    @Test
    public void shouldLoadWithSubreddit() {
        // given
        Subreddit subreddit = mock(Subreddit.class);
        given(topicValidator.isValid(any(Topic.class))).willReturn(true);
        given(subreddits.get(any(SubredditsView.class), eq(0), eq(100), anyObject(), eq(null))).willReturn(singletonList(subreddit));
        given(topicConverter.convert(any(Subreddit.class))).willReturn(aTopic());
        given(topicManager.get(0, 100)).willReturn(singletonList(aTopic()));

        // when
        topicLoaderJob.load();

        // then
        verify(subreddits).get(any(SubredditsView.class), eq(0), eq(100), isA(Subreddit.class), eq(null));
        verify(counterService, times(TopicLoaderJob.SIZE_OF_TOPICS_TO_COLLECT * 2)).increment(anyString());
    }

    @Test
    public void shouldNotLoadWithSubredditBecauseOfErrors() {
        // given
        given(subreddits.get(any(SubredditsView.class), eq(0), eq(100), anyObject(), eq(null))).willThrow(new RuntimeException("test"));
        given(topicConverter.convert(any(Subreddit.class))).willReturn(aTopic());
        given(topicValidator.isValid(any(Topic.class))).willReturn(true);

        // when
        Optional<Collection<Topic>> actual = topicLoaderJob.process(Optional.of(aTopic()), 1);

        // then
        assertThat(actual.isPresent(), is(false));
        verify(subreddits).get(any(SubredditsView.class), eq(0), eq(100), anyObject(), eq(null));
        verify(counterService, never()).increment(anyString());
    }

    @Test
    public void shouldBeReadyToRun() {

        // when
        boolean actual = topicLoaderJob.readyToRun(100);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeReadyBecauseSleeping() {

        // when
        boolean actual = topicLoaderJob.readyToRun(101);

        // then
        assertThat(actual, is(false));
    }
}