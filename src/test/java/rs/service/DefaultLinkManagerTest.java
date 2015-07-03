package rs.service;

import static org.mockito.Mockito.verify;
import static rs.TestFactory.aLink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.dao.LinkDao;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLinkManagerTest {
    @InjectMocks
    private DefaultLinkManager defaultLinkManager;
    @Mock
    private LinkDao linkDao;

    @Test
    public void shouldSave() {

        // when
        defaultLinkManager.save(aLink());

        // then
        verify(linkDao).save(aLink());

    }
}