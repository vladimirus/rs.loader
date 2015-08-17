package rs.loader;

import rs.loader.model.Link;
import rs.loader.model.Topic;

import java.util.Date;

public class TestFactory {

    private TestFactory() {
        //don't instantiate
    }

    public static Link aLink() {
        return aLink("id");
    }

    public static Link aLink(String id) {
        return Link.builder()
                .id(id)
                .title("title")
                .url("http://url")
                .commentsUrl("http://comments")
                .author("author")
                .topic("topic")
                .topicId("topicId")
                .commentCount(1l)
                .score(1l)
                .created(new Date())
                .self(false)
                .nsfw(false)
                .hidden(false)
                .build();
    }

    public static Topic aTopic() {
        return  aTopic("t5_2qh33");
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

    public static Topic.TopicBuilder topicBuilder() {
        return Topic.builder()
                .id("1")
                .displayName("displayName")
                .title("title")
                .created(new Date())
                .updated(new Date())
                .nsfw(false)
                .subscribers(1L)
                .description("this is description")
                .type("public");
    }
}
