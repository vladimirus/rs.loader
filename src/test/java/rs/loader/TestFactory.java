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
        return aLinkBuilder()
                .id(id)
                .build();
    }

    public static Link.LinkBuilder aLinkBuilder() {
        return Link.builder()
                .id("1")
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
                .hidden(false);
    }

    public static Topic aTopic() {
        return  aTopic("t5_2qh33");
    }

    public static Topic aTopic(String id) {
        return aTopic(id, "displayName");
    }

    public static Topic aTopic(String id, String displayName) {
        return aTopicBuilder()
                .id(id)
                .displayName(displayName)
                .build();
    }

    public static Topic.TopicBuilder aTopicBuilder() {
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
