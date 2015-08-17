package rs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rs.loader.RSLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RSLoader.class)
public class RSLoaderTests {
	@Test
	public void contextLoads() {
    }
}
