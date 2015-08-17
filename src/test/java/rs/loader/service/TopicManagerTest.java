package rs.loader.service;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.verify;
import static rs.TestFactory.aTopic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.loader.dao.SimpleDao;
import rs.loader.model.Topic;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class TopicManagerTest {
    @InjectMocks
    private TopicManager topicManager;
    @Mock
    private SimpleDao<Topic> simpleDao;

    @Test
    public void shouldSave() {
        // given
        Topic topic = aTopic();

        // when
        topicManager.save(topic);

        // then
        verify(simpleDao).save(topic);
    }

    @Test
    public void shouldGet() {
        // given
        given(simpleDao.get(0, 10)).willReturn(asList(aTopic("1"), aTopic("1")));

        // when
        Collection<Topic> actual = topicManager.get(0, 10);

        // then
        assertThat(actual, hasSize(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSaveBulk() {

        // when
        topicManager.save(asList(aTopic("1"), aTopic("2")));

        // then
        verify(simpleDao).save(anyCollectionOf(Topic.class));
    }
}