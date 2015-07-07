package rs.service;

import static org.mockito.Mockito.verify;
import static rs.TestFactory.aLink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.dao.SimpleDao;
import rs.model.Link;

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
}