package rs.job;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static rs.TestFactory.aLink;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.model.Link;
import rs.service.SimpleManager;
import rs.service.convert.Converter;

@RunWith(MockitoJUnitRunner.class)
public class LinkLoaderJobTest {
    @InjectMocks
    private LinkLoaderJob linkLoaderJob;
    @Mock
    private Submissions submissions;
    @Mock
    private Converter<Submission, Link> linkConverter;
    @Mock
    private SimpleManager<Link> linkManager;

    @Test
    public void shouldLoad() {
        // given
        Submission submission = mock(Submission.class);
        given(submissions.ofSubreddit(anyString(), any(SubmissionSort.class), eq(-1), eq(100), eq(null), eq(null), eq(true)))
                .willReturn(asList(submission, submission));
        given(linkConverter.convert(any(Submission.class))).willReturn(aLink());

        // when
        linkLoaderJob.load();

        // then
        verify(linkConverter, times(2)).convert(any(Submission.class));
        verify(linkManager, times(2)).save(any(Link.class));
    }

    @Test
    public void shouldLoadTwo() {
        // given
        Submission submission = mock(Submission.class);
        given(submissions.ofSubreddit(anyString(), any(SubmissionSort.class), eq(-1), eq(100), eq(null), eq(null), eq(true)))
                .willReturn(asList(submission, submission, submission));
        given(linkConverter.convert(any(Submission.class)))
                .willReturn(aLink())
                .willThrow(new RuntimeException("test"))
                .willReturn(aLink());

        // when
        linkLoaderJob.load();

        // then
        verify(linkConverter, times(3)).convert(any(Submission.class));
        verify(linkManager, times(2)).save(any(Link.class));
    }
}