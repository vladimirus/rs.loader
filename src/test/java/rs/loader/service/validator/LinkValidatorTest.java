package rs.loader.service.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static rs.loader.TestFactory.aLink;

import org.junit.Test;

public class LinkValidatorTest {

    @Test
    public void shouldBeValid() {
        // given
        LinkValidator linkValidator = new LinkValidator();

        // when
        boolean actual = linkValidator.isValid(aLink());

        // then
        assertThat(actual, is(true));
    }
}