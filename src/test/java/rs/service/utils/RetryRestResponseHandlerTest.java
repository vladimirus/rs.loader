package rs.service.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.jreddit.utils.restclient.Response;
import com.google.common.io.Resources;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class RetryRestResponseHandlerTest {
    @InjectMocks
    private RetryRestResponseHandler retryRestResponseHandler;
    @Mock
    private HttpResponse response;

    @Test
    public void shouldHandleResponse() throws Exception {
        // given
        HttpEntity entity = mock(HttpEntity.class);
        given(response.getEntity()).willReturn(entity);
        given(entity.getContent()).willReturn(fixture("subreddits-01.json"));

        // when
        Response actual = retryRestResponseHandler.handleResponse(response);

        // then
        assertThat(actual, notNullValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldHandleResponseWithMoreContent() throws Exception {
        // given
        HttpEntity entity = mock(HttpEntity.class);
        given(response.getEntity()).willReturn(entity);
        given(entity.getContent()).willReturn(fixture("subreddits-02.json"));

        // when
        Response actual = retryRestResponseHandler.handleResponse(response);
        Map<String, Object> actualMap = (Map<String, Object>) actual.getResponseObject();

        // then
        assertThat(actual, notNullValue());
        assertThat(actualMap.get("data"), notNullValue());
        assertThat(actualMap.get("kind"), is("Listing"));
    }

    @SneakyThrows
    private InputStream fixture(String filename) {
        return new ByteArrayInputStream(Resources.toString(Resources.getResource(filename), UTF_8).trim().getBytes(UTF_8));
    }

}