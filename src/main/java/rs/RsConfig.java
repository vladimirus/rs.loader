package rs;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
public class RsConfig {

    @Bean
    public RestClient redditClient() {
        RestClient restClient = new HttpRestClient();
        restClient.setUserAgent("rs client yo!");
        return restClient;
    }

    @Bean
    public User redditUser() {
        return new User(redditClient(), "", "");
    }
}
