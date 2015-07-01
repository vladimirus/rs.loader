package rs.dao;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import rs.model.Link;


@RunWith(MockitoJUnitRunner.class)
public class ElasticDaoTest {
    @Mock
    private ElasticsearchTemplate template;
    @InjectMocks
    private ElasticDao elasticDao;

    @Test
    public void shouldSave() {

        // when
        elasticDao.save(new Link("id", "title", "url"));

        // then
        verify(template).index(isA(IndexQuery.class));
    }
}