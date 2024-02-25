package edu.java.configuration;

import edu.java.contributor.sources.GithubInfoContributor;
import edu.java.contributor.sources.StackOverflowInfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfig {
    @Bean
    public GithubInfoContributor githubInfoSupplier() {
        return new GithubInfoContributor();
    }

    @Bean
    public StackOverflowInfoContributor stackOverflowInfoSupplier() {
        return new StackOverflowInfoContributor();
    }
}
