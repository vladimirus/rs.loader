package rs.loader.job;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rs.loader.service.LinkManager;

@RunWith(MockitoJUnitRunner.class)
public class MissingDomainsLoaderJobTest {
    @InjectMocks
    private MissingDomainsLoaderJob missingDomainsLoaderJob;
    @Mock
    private LinkManager linkManager;

    @Test
    public void shouldGetHost() {

        // when
        String actual = missingDomainsLoaderJob.getDomain("http://i.imgur.com/e9uOUi7.png");

        // then
        assertThat(actual, is("i.imgur.com"));
    }

    @Test
    public void shouldNotGetHost() {

        // when
        String actual = missingDomainsLoaderJob.getDomain("something");

        // then
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldLoad() {
        // given
        missingDomainsLoaderJob.enabled = true;

        // when
        missingDomainsLoaderJob.load();

        //then
        verify(linkManager).getMissingDomains(0, 1000);
    }

    @Test
    public void shouldNotLoad() {
        // given
        missingDomainsLoaderJob.enabled = false;

        // when
        missingDomainsLoaderJob.load();

        //then
        verify(linkManager, never()).getMissingDomains(0, 1000);
    }

    @Test
    public void shouldGetDomain() {
        // when
        String actual = missingDomainsLoaderJob.getDomain("http://smile.amazon.com/gp/product/B003CMWX0S?ref_=sr_1_5&amp;s=apparel&amp;tag=tor0b-20&amp;qid=1444253156&amp;sr=1-5&amp;nodeID=7147441011&amp;keywords=winter gloves&amp;pldnSite=1");

        // then
        assertThat(actual, is("smile.amazon.com"));
    }
}