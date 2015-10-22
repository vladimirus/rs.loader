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
import rs.loader.dao.LinkDao;
import rs.loader.dao.SimpleDao;
import rs.loader.model.Comment;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class CommentManagerTest {
    @InjectMocks
    private CommentManager commentManager;
    @Mock
    private SimpleDao<Comment> simpleDao;
    @Mock
    private LinkDao linkDao;

    @Test
    public void shouldSave() {
        // given
        Comment comment = aComment("1");

        // when
        commentManager.save(comment);

        // then
        verify(simpleDao).save(comment);
        verify(linkDao).updateComments(anyCollectionOf(Comment.class));
    }

    @Test
    public void shouldGet() {
        // given
        given(simpleDao.get(0, 10)).willReturn(asList(aComment("1"), aComment("2")));

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
        verify(simpleDao).save(anyCollectionOf(Comment.class));
        verify(linkDao).updateComments(anyCollectionOf(Comment.class));
    }
}