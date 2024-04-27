package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.supplier.GithubConfig;
import edu.java.configuration.supplier.StackOverflowConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, GithubConfig.class, StackOverflowConfig.class })
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
