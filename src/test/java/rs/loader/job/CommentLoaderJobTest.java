package rs.loader.job;

import static com.github.jreddit.retrieval.params.CommentSort.TOP;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static rs.loader.TestFactory.aComment;

import com.github.jreddit.retrieval.Comments;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import rs.loader.model.Comment;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

import java.util.Collection;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CommentLoaderJobTest {
    @InjectMocks
    private CommentLoaderJob commentLoaderJob;
    @Mock
    private Comments comments;
    @Mock
    private Converter<com.github.jreddit.entity.Comment, Comment> commentConverter;
    @Mock
    private Validator<Comment> commentValidator;
    @Mock
    private CounterService counterService;
    @Mock
    private GaugeService gaugeService;

    @Test
    public void shouldProcess() {
        // given
        com.github.jreddit.entity.Comment returned = mock(com.github.jreddit.entity.Comment.class);
        Comment comment = aComment("type-id");
        given(comments.ofSubmission("type-id", null, -1, -1, -1, TOP)).willReturn(singletonList(returned));
        given(commentConverter.convert(returned)).willReturn(comment);
        given(commentValidator.isValid(comment)).willReturn(true);

        // when
        Optional<Collection<Comment>> actual = commentLoaderJob.process("type-id", 1);

        // then
        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get().iterator().next(), is(comment));
    }


}