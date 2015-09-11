package rs.loader.service.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static rs.loader.TestFactory.aTopicBuilder;

import org.junit.Test;

public class TopicValidatorTest {

    @Test
    public void shouldBeValid() {
        // given
        TopicValidator topicValidator = new TopicValidator();
        topicValidator.minNumberOfSubscribers = 2;
        topicValidator.maxNumberOfSubscribers = 10;

        // when
        boolean actual = topicValidator.isValid(aTopicBuilder().subscribers(3L).build());

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void shouldBeInvalid() {
        // given
        TopicValidator topicValidator = new TopicValidator();
        topicValidator.minNumberOfSubscribers = 2;
        topicValidator.maxNumberOfSubscribers = 10;

        // when
        boolean actual = topicValidator.isValid(aTopicBuilder().subscribers(1L).build());

        // then
        assertThat(actual, is(false));
    }
}