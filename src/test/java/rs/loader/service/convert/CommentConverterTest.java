package rs.loader.service.convert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import rs.loader.model.Comment;

public class CommentConverterTest {
    private CommentConverter commentConverter;

    @Before
    public void setup() {
        this.commentConverter = new CommentConverter();
    }

    @Test
    public void shouldConvert() {
        // given
        com.github.jreddit.entity.Comment comment = mock(com.github.jreddit.entity.Comment.class);
        given(comment.getFullName()).willReturn("id");
        given(comment.getAuthor()).willReturn("author");
        given(comment.getParentId()).willReturn("parentId");
        given(comment.getSubreddit()).willReturn("topic");
        given(comment.getSubredditId()).willReturn("topicId");
        given(comment.getLinkId()).willReturn("linkId");
        given(comment.getBodyHTML()).willReturn("bodyHtml");
        given(comment.getBody()).willReturn("body");
        given(comment.getCreatedUTC()).willReturn(1.0);
        given(comment.getScore()).willReturn(1);

        // when
        Comment actual = commentConverter.convert(comment);

        // then
        assertThat(actual.getId(), is("id"));
        assertThat(actual.getBodyHTML(), is("bodyHtml"));
    }
}