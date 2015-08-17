package rs.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class RSLoader {

    public static void main(String[] args) {
        SpringApplication.run(RSLoader.class, args);
    }
}
