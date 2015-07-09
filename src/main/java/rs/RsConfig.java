package rs;

import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.Subreddits;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

    @Bean
    public Submissions submissions() {
        return new Submissions(redditClient(), redditUser());
    }

    @Bean
    public Subreddits subreddits() {
        return new Subreddits(redditClient(), redditUser());
    }
}
