package rs.service;

import static org.mockito.Mockito.verify;
import static rs.TestFactory.aLink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.dao.SimpleDao;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLinkManagerTest {
    @InjectMocks
    private LinkManager linkManager;
    @Mock
    private SimpleDao simpleDao;

    @Test
    public void shouldSave() {

        // when
        linkManager.save(aLink());

        // then
        verify(simpleDao).save(aLink());

    }
}