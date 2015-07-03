package rs;

import rs.model.Link;

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
}
