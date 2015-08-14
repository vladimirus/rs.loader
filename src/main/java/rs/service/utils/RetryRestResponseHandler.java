package rs.service.utils;


import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.io.IOUtils.closeQuietly;

import com.github.jreddit.utils.restclient.Response;
import com.github.jreddit.utils.restclient.RestResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;

public class RetryRestResponseHandler implements ResponseHandler<Response> {
    private Logger log = Logger.getLogger(RetryRestResponseHandler.class);

    @Override
    public Response handleResponse(HttpResponse response) throws IOException {
        return handleResponse(content(response), response, 0);
    }

    public Response handleResponse(String responseStr, HttpResponse response, Integer errorCount) {
        try {
            return parse(responseStr, response);
        } catch (Throwable e) {
            if (errorCount < 10) {
                log.info(String.format("Error parsing trying again, count: %d", errorCount));
                sleepUninterruptibly(6, SECONDS);
                return handleResponse(responseStr, response, errorCount + 1);
            } else {
                log.error("Error parsing response from Reddit", e);
                log.error("Response was " + responseStr);
            }
        }
        return null;
    }

    private Response parse(String content, HttpResponse response) throws ParseException {
        return new RestResponse(content, new JSONParser().parse(content), response);
    }

    private String content(HttpResponse response) throws IOException {
        InputStream responseStream = null;
        try {
            responseStream = response.getEntity().getContent();
            return IOUtils.toString(responseStream, "UTF-8");
        } finally {
            closeQuietly(responseStream);
        }
    }
}
