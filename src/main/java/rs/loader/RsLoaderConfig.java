package rs.loader;

import static org.apache.http.client.config.CookieSpecs.IGNORE_COOKIES;

import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Comments;
import com.github.jreddit.retrieval.ExtendedComments;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.Subreddits;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.loader.service.utils.RetryRestResponseHandler;

@Configuration
public class RsLoaderConfig {

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setCookieSpec(IGNORE_COOKIES)
                .setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .setConnectTimeout(60000)
                .setMaxRedirects(10)
                .build();
    }

    @Bean
    public HttpClient httpClient(RequestConfig config) {
        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setUserAgent(userAgent())
                .build();
    }

    @Bean
    public RestClient redditClient() {
        HttpRestClient client = new HttpRestClient(httpClient(requestConfig()), new RetryRestResponseHandler());
        client.setUserAgent(userAgent());
        return client;
    }

    private String userAgent() {
        return "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0";
    }

    @Bean
    public User redditUser() {
        return new User(redditClient(), "", "");
    }

    @Bean
    public Submissions submissions() {
        return new Submissions(redditClient(), redditUser());
    }

    @Bean
    public Subreddits subreddits() {
        return new Subreddits(redditClient(), redditUser());
    }

    @Bean
    public Comments comments() {
        return new Comments(redditClient(), redditUser());
    }

    @Bean
    public ExtendedComments extendedComments() {
        return new ExtendedComments(comments());
    }
}
