package rs.loader.dao;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static rs.loader.TestFactory.aComment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

@RunWith(MockitoJUnitRunner.class)
public class CommentDaoTest {
    @InjectMocks
    private CommentDao commentDao;
    @Mock
    private ElasticsearchTemplate template;

    @Before
    public void setup() {
        commentDao.indexName = "rs";
    }

    @Test
    public void shouldSave() {

        // when
        commentDao.save(aComment("1"));

        // then
        verify(template).index(isA(IndexQuery.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGet() {
        // given
        FacetedPage page = mock(FacetedPage.class);
        given(template.queryForPage(isA(SearchQuery.class), isA(Class.class))).willReturn(page);

        // when
        commentDao.get(0, 10);

        // then
        verify(template).queryForPage(isA(SearchQuery.class), isA(Class.class));
    }

    @Test
    public void shouldSaveCollection() {

        // when
        commentDao.save(asList(aComment("1"), aComment("2")));

        // then
        verify(template).bulkIndex(any());
    }
}