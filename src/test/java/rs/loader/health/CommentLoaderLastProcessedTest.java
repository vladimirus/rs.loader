package rs.loader.health;

import static java.time.LocalDateTime.now;
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
public class CommentLoaderLastProcessedTest {
    @InjectMocks
    private CommentLoaderLastProcessed commentLoaderLastProcessed;
    @Mock
    private CommentLoaderJob commentLoaderJob;

    @Test
    public void shouldBeHealthy() {
        // when
        given(commentLoaderJob.getLastProcessed()).willReturn(now().minusMinutes(1));

        // when
        Health actual = commentLoaderLastProcessed.health();

        // then
        assertThat(actual.getStatus(), is(UP));
    }

    @Test
    public void shouldNotBeHealthy() {
        // when
        given(commentLoaderJob.getLastProcessed()).willReturn(now().minusMinutes(11));

        // when
        Health actual = commentLoaderLastProcessed.health();

        // then
        assertThat(actual.getStatus(), is(DOWN));
    }
}