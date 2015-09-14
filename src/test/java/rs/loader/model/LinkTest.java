package rs.loader.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static rs.loader.TestFactory.aLink;

import org.junit.Test;

public class LinkTest {

    @Test
    public void shouldGetIdWithoutType() {
        // given
        Link link = aLink("type_id123");

        // when
        String actual = link.getIdWithoutType();

        // then
        assertThat(actual, is("id123"));
    }

    @Test
    public void shouldNotGetIdWithoutType() {
        // given
        Link link = aLink("typeid123");

        // when
        String actual = link.getIdWithoutType();

        // then
        assertThat(actual, is("typeid123"));
    }
}