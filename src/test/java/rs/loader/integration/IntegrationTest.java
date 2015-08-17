package rs.loader.integration;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class IntegrationTest {
    @Test
    public void shouldBeHealthy() {
        get("http://rs.loader.supr.me/manage/health")
                .prettyPeek()
                .then()
                .body("status", is("UP"));
    }
}
