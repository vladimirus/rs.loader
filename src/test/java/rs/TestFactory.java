package rs;

import rs.model.Link;
import rs.model.Topic;

public class TestFactory {

    private TestFactory() {
        //don't instantiate
    }

    public static Link aLink() {
        return Link.builder()
                .id("id")
                .title("title")
                .url("http://url").build();
    }

    public static Topic aTopic() {
        return Topic.builder()
                .id("id").build();
    }
}
