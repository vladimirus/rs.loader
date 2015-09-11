package rs.loader;

import com.github.jreddit.retrieval.ExtendedComments;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RsLoader.class)
public class RsLoaderTests {

	@Autowired
	private ExtendedComments comments;

	@Test
	public void contextLoads() {

//		List<Comment> list = comments.ofSubmission("3cmvzp", CommentSort.TOP, -1, null);

	}
}
