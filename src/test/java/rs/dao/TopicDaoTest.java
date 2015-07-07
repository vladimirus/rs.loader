package rs.dao;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static rs.TestFactory.aTopic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;


@RunWith(MockitoJUnitRunner.class)
public class TopicDaoTest {
    @InjectMocks
    private TopicDao topicDao;
    @Mock
    private ElasticsearchTemplate template;

    @Test
    public void shouldSave() {

        // when
        topicDao.save(aTopic());

        // then
        verify(template).index(isA(IndexQuery.class));
    }
}