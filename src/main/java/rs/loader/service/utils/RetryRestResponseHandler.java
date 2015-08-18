package rs.loader.service.utils;


import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.io.IOUtils.closeQuietly;

import com.github.jreddit.utils.restclient.Response;
import com.github.jreddit.utils.restclient.RestResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;

public class RetryRestResponseHandler implements ResponseHandler<Response> {
    private Logger log = Logger.getLogger(RetryRestResponseHandler.class);

    @Override
    public Response handleResponse(HttpResponse response) throws IOException {
        return handleResponse(content(response), response);
    }

    public Response handleResponse(String content, HttpResponse response) {
        try {
            return new RestResponse(content, new JSONParser().parse(content), response);
        } catch (Throwable e) {
            log.error(format("Error parsing response, status code was %d. Sleeping for %d seconds...", statusCode(response), 10));
//            log.error("Error parsing response", e);
//            log.error("Response was " + responseStr);
            sleepUninterruptibly(10, SECONDS); //slow down
        }
        return null;
    }

    private int statusCode(HttpResponse response) {
        try {
            return response.getStatusLine().getStatusCode();
        } catch (Exception ignore) {
            log.error("Error getting status code", ignore);
            return 0;
        }
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
