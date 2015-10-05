package rs.loader.service.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static rs.loader.TestFactory.aCommentBuilder;

import org.junit.Test;

public class CommentValidatorTest {

    @Test
    public void shouldBeValid() {
        // given
        CommentValidator commentValidator = new CommentValidator();
        commentValidator.minScore = 2;
        commentValidator.maxScore = 10;

        // when
        boolean actual = commentValidator.isValid(aCommentBuilder().score(3L).build());

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldNotBeValid() {
        // given
        CommentValidator commentValidator = new CommentValidator();
        commentValidator.minScore = 2;
        commentValidator.maxScore = 10;

        // when
        boolean actual = commentValidator.isValid(aCommentBuilder().score(1L).build());

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void shouldBeValidBecauseCommentDeleted() {
        // given
        CommentValidator commentValidator = new CommentValidator();
        commentValidator.minScore = 2;
        commentValidator.maxScore = 10;

        // when
        boolean actual = commentValidator.isValid(aCommentBuilder().score(3L).body("[deleted]").build());

        // then
        assertThat(actual, is(false));
    }

}