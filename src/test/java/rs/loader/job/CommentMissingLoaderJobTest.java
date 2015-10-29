package rs.loader.job;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.AsyncEventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.loader.service.LinkManager;

@RunWith(MockitoJUnitRunner.class)
public class CommentMissingLoaderJobTest {
    @InjectMocks
    private CommentMissingLoaderJob commentMissingLoaderJob;
    @Mock
    private AsyncEventBus eventBus;
    @Mock
    private CommentLoaderJob commentLoaderJob;
    @Mock
    private LinkManager linkManager;

    @Test
    public void shouldLoad() {
        // given
        commentMissingLoaderJob.enabled = true;
        given(commentLoaderJob.queueSize()).willReturn(1);

        // when
        commentMissingLoaderJob.load();

        //then
        verify(linkManager).getMissingComments(0, 100);
    }

    @Test
    public void shouldNotLoad() {
        // given
        commentMissingLoaderJob.enabled = false;
        given(commentLoaderJob.queueSize()).willReturn(1);

        // when
        commentMissingLoaderJob.load();

        //then
        verify(linkManager, never()).getMissingComments(0, 100);
    }

    @Test
    public void shouldNotLoadBecauseOfQueueSize() {
        // given
        commentMissingLoaderJob.enabled = true;
        given(commentLoaderJob.queueSize()).willReturn(50);

        // when
        commentMissingLoaderJob.load();

        //then
        verify(linkManager, never()).getMissingComments(0, 100);
    }
}