package rs.service;

import static org.mockito.Mockito.verify;
import static rs.TestFactory.aTopic;

import com.google.common.eventbus.AsyncEventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.dao.SimpleDao;
import rs.model.Topic;

@RunWith(MockitoJUnitRunner.class)
public class TopicManagerTest {
    @InjectMocks
    private TopicManager topicManager;
    @Mock
    private SimpleDao<Topic> simpleDao;
    @Mock
    private AsyncEventBus eventBus;

    @Test
    public void shouldSave() {
        // given
        Topic topic = aTopic();

        // when
        topicManager.save(topic);

        // then
        verify(simpleDao).save(topic);
        verify(eventBus).post(topic);
    }
}