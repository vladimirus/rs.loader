package rs.loader.service;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.verify;
import static rs.loader.TestFactory.aComment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.loader.dao.CommentDao;
import rs.loader.model.Comment;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class CommentManagerTest {
    @InjectMocks
    private CommentManager commentManager;
    @Mock
    private CommentDao commentDao;

    @Test
    public void shouldSave() {
        // given
        Comment comment = aComment("1");

        // when
        commentManager.save(comment);

        // then
        verify(commentDao).save(comment);
    }

    @Test
    public void shouldGet() {
        // given
        given(commentDao.get(0, 10)).willReturn(asList(aComment("1"), aComment("2")));

        // when
        Collection<Comment> actual = commentManager.get(0, 10);

        // then
        assertThat(actual, hasSize(2));
    }

    @Test
    public void shouldSaveBulk() {

        // when
        commentManager.save(asList(aComment("1"), aComment("2")));

        // then
        verify(commentDao).save(anyCollectionOf(Comment.class));
    }

    @Test
    public void shouldGetForLinkId() {

        // when
        commentManager.getCommentsForLinkId("linkId", 100000);

        // then
        verify(commentDao).getCommentsForLinkId(0, 100000, "linkId");
    }
}