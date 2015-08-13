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
        try {
            return parse(response);
        } catch (ParseException e) {
            log.error("Error parsing response from Reddit", e);
        }
        return null;
    }

    private Response parse(HttpResponse httpResponse) throws IOException, ParseException {
        InputStream responseStream = null;
        try {
            responseStream = httpResponse.getEntity().getContent();
            String content = IOUtils.toString(responseStream, "UTF-8");
            Object responseObject = jsonParser.parse(content);
            return new RestResponse(content, responseObject, httpResponse);
        } finally {
            closeQuietly(responseStream);
        }
    }
}
