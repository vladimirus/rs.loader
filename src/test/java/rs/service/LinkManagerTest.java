package rs.service;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.verify;
import static rs.TestFactory.aLink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.dao.SimpleDao;
import rs.model.Link;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class LinkManagerTest {
    @InjectMocks
    private LinkManager linkManager;
    @Mock
    private SimpleDao<Link> simpleDao;

    @Test
    public void shouldSave() {
        // given
        Link link = aLink();

        // when
        linkManager.save(link);

        // then
        verify(simpleDao).save(link);
    }

    @Test
    public void shouldGet() {
        // given
        given(simpleDao.get(0, 10)).willReturn(asList(aLink(), aLink()));

        // when
        Collection<Link> actual = linkManager.get(0, 10);

        // then
        assertThat(actual, hasSize(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSaveBulk() {

        // when
        linkManager.save(asList(aLink("1"), aLink("2")));

        // then
        verify(simpleDao).save(anyCollection());
    }
}