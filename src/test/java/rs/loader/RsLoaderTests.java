package rs.loader;

import com.github.jreddit.retrieval.Comments;
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
	private ExtendedComments extendedComments;

	@Autowired
	private Comments comments;

	@Test
	public void contextLoads() {
//        StopWatch timer = new StopWatch();
//        timer.start();
//
//        List<Comment> list2 = comments.ofSubmission("3m2u3g", null, -1, -1, -1, CommentSort.TOP);
//        timer.split();
//        System.out.println(timer.toString());
//
//		List<Comment> list = extendedComments.ofSubmission("3m2u3g", CommentSort.TOP, -1, null);
//        timer.stop();
//
//        System.out.println(timer.toString());
//        System.out.println("end");

	}
}
