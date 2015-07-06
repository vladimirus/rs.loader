package rs;

import rs.model.Link;
import rs.model.Topic;

/**
 * Created by vladimir.
 */
public class TestFactory {

    private TestFactory() {
        //don't instantiate
    }

    public static Link aLink() {
        return new Link("id", "title", "http://url");
    }

    public static Topic aTopic() {
        return new Topic("id");
    }
}
