package rs.health;

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
import rs.job.LinkLoaderJob;

@RunWith(MockitoJUnitRunner.class)
public class LinkLoaderQueueSizeTest {
    @InjectMocks
    private LinkLoaderQueueSize linkLoaderQueueSize;
    @Mock
    private LinkLoaderJob linkLoaderJob;

    @Test
    public void shouldBeHealthy() {
        // given
        given(linkLoaderJob.getQueueSize()).willReturn(10);

        // when
        Health actual = linkLoaderQueueSize.health();

        // then
        assertThat(actual.getStatus(), is(UP));
    }

    @Test
    public void shouldNotBeHealthy() {
        // given
        given(linkLoaderJob.getQueueSize()).willReturn(9);

        // when
        Health actual = linkLoaderQueueSize.health();

        // then
        assertThat(actual.getStatus(), is(DOWN));
    }
}