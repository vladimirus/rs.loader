package rs;

import rs.model.Link;
import rs.model.Topic;

import java.util.Date;

public class TestFactory {

    private TestFactory() {
        //don't instantiate
    }

    public static Link aLink() {
        return Link.builder()
                .id("id")
                .title("title")
                .url("http://url")
                .commentsUrl("http://comments")
                .author("author")
                .subreddit("subreddit")
                .subredditId("subredditId")
                .commentCount(1l)
                .score(1l)
                .created(new Date())
                .self(false)
                .nsfw(false)
                .hidden(false)
                .build();
    }

    public static Topic aTopic() {
        return  aTopic("id");
    }

    public static Topic aTopic(String id) {
        return Topic.builder()
                .id(id)
                .displayName("displayName")
                .title("title")
                .created(new Date())
                .updated(new Date())
                .nsfw(false)
                .subscribers(1L)
                .description("this is description")
                .type("public")
                .build();
    }
}
