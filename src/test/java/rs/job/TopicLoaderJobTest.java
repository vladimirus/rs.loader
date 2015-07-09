package rs.job;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static rs.TestFactory.aTopic;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.Subreddits;
import com.github.jreddit.retrieval.params.SubredditsView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.model.Topic;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

@RunWith(MockitoJUnitRunner.class)
public class TopicLoaderJobTest {
    @InjectMocks
    private TopicLoaderJob topicLoaderJob;
    @Mock
    private Subreddits subreddits;
    @Mock
    private Converter<Subreddit, Topic> topicConverter;
    @Mock
    private SimpleManager<Topic> topicManager;

    @Test
    public void shouldLoad() {
        // given
        Subreddit subreddit = mock(Subreddit.class);
        given(subreddits.get(any(SubredditsView.class), eq(0), eq(100), eq(null), eq(null))).willReturn(asList(subreddit, subreddit));
        given(topicConverter.convert(any(Subreddit.class))).willReturn(aTopic());

        // when
        topicLoaderJob.load();

        // then
        verify(topicConverter, times(2)).convert(any(Subreddit.class));
        verify(topicManager, times(2)).save(any(Topic.class));
    }

    @Test
    public void shouldGetLast() {

        // when
        Subreddit actual = topicLoaderJob.lastSubreddit(aTopic("t5_2qh33"));

        // then
        assertThat(actual.getFullName(), is("t5_2qh33"));
    }

    @Test
    public void shouldNotGetLast() {

        // when
        Subreddit actual = topicLoaderJob.lastSubreddit(null);

        // then
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldInitLast() {

        // when
        topicLoaderJob.initLast();

        // then
        verify(topicManager).get(isA(Integer.class), isA(Integer.class));
    }

    @Test
    public void shouldNotInitLast() {
        // given
        topicLoaderJob.last = aTopic();

        // when
        topicLoaderJob.initLast();

        // then
        verify(topicManager, never()).get(isA(Integer.class), isA(Integer.class));
    }

    @Test
    public void shouldLoadWithSubreddit() {
        // given
        topicLoaderJob.last = aTopic();
        Subreddit subreddit = mock(Subreddit.class);
        given(subreddits.get(any(SubredditsView.class), eq(0), eq(100), anyObject(), eq(null))).willReturn(asList(subreddit, subreddit));
        given(topicConverter.convert(any(Subreddit.class))).willReturn(aTopic());

        // when
        topicLoaderJob.load();

        // then
        verify(subreddits).get(any(SubredditsView.class), eq(0), eq(100), isA(Subreddit.class), eq(null));
    }

    @Test
    public void shouldNotLoadWithSubredditBecauseOfErrors() {
        // given
        topicLoaderJob.last = aTopic();
        topicLoaderJob.numberOrErrorsInARow = 10;
        Subreddit subreddit = mock(Subreddit.class);
        given(subreddits.get(any(SubredditsView.class), eq(0), eq(100), anyObject(), eq(null))).willReturn(asList(subreddit, subreddit));
        given(topicConverter.convert(any(Subreddit.class))).willReturn(aTopic());

        // when
        topicLoaderJob.load();

        // then
        verify(subreddits).get(any(SubredditsView.class), eq(0), eq(100), eq(null), eq(null));
    }

}