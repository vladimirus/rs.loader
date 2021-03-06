package rs.loader.service;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static rs.loader.TestFactory.aLink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.loader.dao.LinkDao;
import rs.loader.dao.SimpleDao;
import rs.loader.model.Link;
import rs.loader.model.Suggestion;
import rs.loader.service.convert.SuggestionConverter;

import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class LinkManagerTest {
    @InjectMocks
    private LinkManager linkManager;
    @Mock
    private LinkDao linkDao;
    @Mock
    private SimpleDao<Suggestion> suggestionDao;
    @Mock
    private SuggestionConverter suggestionLinkConverter;
    @Mock
    private CommentManager commentManager;

    @Test
    public void shouldSave() {
        // given
        Link link = aLink();

        // when
        linkManager.save(link);

        // then
        verify(linkDao).save(link);
        verify(commentManager).getCommentsForLinkId(anyString(), anyInt());
    }

    @Test
    public void shouldGet() {
        // given
        given(linkDao.get(0, 10)).willReturn(asList(aLink(), aLink()));

        // when
        Collection<Link> actual = linkManager.get(0, 10);

        // then
        assertThat(actual, hasSize(2));
    }

    @Test
    public void shouldSaveBulk() {

        // when
        linkManager.save(asList(aLink("1"), aLink("2")));

        // then
        verify(linkDao).save(anyCollectionOf(Link.class));
        verify(suggestionDao).save(anyCollectionOf(Suggestion.class));
        verify(suggestionLinkConverter, times(2)).convert(any());
        verify(commentManager, times(2)).getCommentsForLinkId(anyString(), anyInt());
    }

    @Test
    public void shouldGetMissingComments() {
        // given
        given(linkDao.getMissingComments(0, 10)).willReturn(asList(aLink(), aLink()));

        // when
        Collection<Link> actual = linkManager.getMissingComments(0, 10);

        // then
        assertThat(actual, hasSize(2));
    }

    @Test
    public void shouldGetMissingDomain() {
        // given
        given(linkDao.getMissingDomains(0, 10)).willReturn(asList(aLink(), aLink()));

        // when
        Collection<Link> actual = linkManager.getMissingDomains(0, 10);

        // then
        assertThat(actual, hasSize(2));
    }
}