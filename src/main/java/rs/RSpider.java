package rs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class RSpider {

    public static void main(String[] args) {
        SpringApplication.run(RSpider.class, args);
    }
}