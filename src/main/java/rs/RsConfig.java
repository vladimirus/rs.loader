package rs;

import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.Subreddits;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;
import com.github.jreddit.utils.restclient.RestResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RsConfig {

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
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
                .setUserAgent("rs client yo!")
                .build();
    }

    @Bean
    public RestClient redditClient() {
        return new HttpRestClient(httpClient(requestConfig()), new RestResponseHandler());
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
}
