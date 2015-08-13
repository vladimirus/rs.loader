package rs.service.utils;


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

public class RsRestResponseHandler implements ResponseHandler<Response> {
    private Logger log = Logger.getLogger(RsRestResponseHandler.class);
    private final JSONParser jsonParser;

    public RsRestResponseHandler() {
        this.jsonParser = new JSONParser();
    }

    @Override
    public Response handleResponse(HttpResponse response) throws IOException {
        String responseStr = null;
        try {
            responseStr = content(response);
            return parse(responseStr, response);
        } catch (ParseException e) {
            log.error("Error parsing response from Reddit", e);
            log.error("Response was " + responseStr, e);
        }
        return null;
    }

    private Response parse(String content, HttpResponse response) throws ParseException {
        return new RestResponse(content, jsonParser.parse(content), response);
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
