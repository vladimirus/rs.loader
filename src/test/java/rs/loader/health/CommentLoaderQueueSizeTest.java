package rs.loader.health;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import rs.loader.job.CommentLoaderJob;

@RunWith(MockitoJUnitRunner.class)
public class CommentLoaderQueueSizeTest {
    @InjectMocks
    private CommentLoaderQueueSize commentLoaderQueueSize;
    @Mock
    private CommentLoaderJob commentLoaderJob;

    @Test
    public void shouldBeHealthy() {
        // given
        given(commentLoaderJob.queueSize()).willReturn(10);

        // when
        Health actual = commentLoaderQueueSize.health();

        // then
        assertThat(actual.getStatus(), is(UP));
    }

    @Test
    public void shouldNotBeHealthy() {
        // given
        given(commentLoaderJob.queueSize()).willReturn(9);

        // when
        Health actual = commentLoaderQueueSize.health();

        // then
        assertThat(actual.getStatus(), is(DOWN));
    }
}