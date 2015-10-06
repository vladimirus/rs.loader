package rs.loader.job;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static rs.loader.TestFactory.aTopic;
import static rs.loader.job.TopicTopLoaderJob.SIZE_OF_TOPICS_TO_COLLECT;

import com.google.common.eventbus.AsyncEventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.loader.model.Topic;
import rs.loader.service.TopicManager;

@RunWith(MockitoJUnitRunner.class)
public class TopicTopLoaderJobTest {
    @InjectMocks
    private TopicTopLoaderJob topicTopLoaderJob;
    @Mock
    private TopicManager topicManager;
    @Mock
    private LinkLoaderJob linkLoaderJob;
    @Mock
    private AsyncEventBus eventBus;

    @Before
    public void setup() {
        SIZE_OF_TOPICS_TO_COLLECT = 5;
        topicTopLoaderJob.enabled = true;
    }

    @Test
    public void shouldLoad() {
        // given
        given(linkLoaderJob.queueSize()).willReturn(0, 1, 2);
        given(topicManager.getTop(SIZE_OF_TOPICS_TO_COLLECT)).willReturn(asList(aTopic("1"), aTopic("2")));

        // when
        topicTopLoaderJob.load();

        // then
        verify(topicManager).getTop(SIZE_OF_TOPICS_TO_COLLECT);
        verify(eventBus, times(2)).post(isA(Topic.class));
    }

    @Test
    public void shouldNotLoad() {
        // given
        topicTopLoaderJob.enabled = false;
        given(linkLoaderJob.queueSize()).willReturn(0, 1, 2);
        given(topicManager.getTop(SIZE_OF_TOPICS_TO_COLLECT)).willReturn(asList(aTopic("1"), aTopic("2")));

        // when
        topicTopLoaderJob.load();

        // then
        verify(topicManager, never()).getTop(SIZE_OF_TOPICS_TO_COLLECT);
        verify(eventBus, never()).post(isA(Topic.class));
    }
}