package rs.loader.service.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static rs.loader.TestFactory.aLinkBuilder;

import org.junit.Test;

public class LinkValidatorTest {

    @Test
    public void shouldBeValid() {
        // given
        LinkValidator linkValidator = new LinkValidator();
        linkValidator.minScore = 2;
        linkValidator.maxScore = 10;

        // when
        boolean actual = linkValidator.isValid(aLinkBuilder().score(3L).build());

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeValid() {
        // given
        LinkValidator linkValidator = new LinkValidator();
        linkValidator.minScore = 2;
        linkValidator.maxScore = 10;

        // when
        boolean actual = linkValidator.isValid(aLinkBuilder().score(1L).build());

        // then
        assertThat(actual, is(false));
    }
}