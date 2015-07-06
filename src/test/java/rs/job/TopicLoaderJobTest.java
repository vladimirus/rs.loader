package rs.job;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
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

}