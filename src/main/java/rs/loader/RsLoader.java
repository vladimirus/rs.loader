package rs.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class RsLoader {

    public static void main(String[] args) {
        SpringApplication.run(RsLoader.class, args);
    }
}
