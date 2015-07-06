package rs.dao;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static rs.TestFactory.aLink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;


@RunWith(MockitoJUnitRunner.class)
public class LinkDaoTest {
    @InjectMocks
    private LinkDao linkDao;
    @Mock
    private ElasticsearchTemplate template;

    @Test
    public void shouldSave() {

        // when
        linkDao.save(aLink());

        // then
        verify(template).index(isA(IndexQuery.class));
    }
}