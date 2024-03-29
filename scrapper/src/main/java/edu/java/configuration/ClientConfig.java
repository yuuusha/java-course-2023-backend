package edu.java.configuration;

import edu.java.contributor.sources.GithubInfoProvider;
import edu.java.contributor.sources.StackOverflowInfoProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfig {
    @Bean
    public GithubInfoProvider githubInfoSupplier() {
        return new GithubInfoProvider();
    }

    @Bean
    public StackOverflowInfoProvider stackOverflowInfoSupplier() {
        return new StackOverflowInfoProvider();
    }
}
