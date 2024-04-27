package edu.java.configuration;

import edu.java.client.bot.BotClient;
import edu.java.util.retry.RetryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class BotClientConfiguration {

    @Value("${bot.url:http://localhost:8090}")
    private String botUrl;

    @Bean
    public BotClient botClient(RetryQueryConfiguration retryQueryConfiguration) {
        WebClient webClient = WebClient.builder()
            .defaultStatusHandler(httpStatusCode -> true, clientResponse -> Mono.empty())
            .defaultHeader("Content-Type", "application/json")
            .filter(RetryFactory.createFilter(retryQueryConfiguration, "bot"))
            .baseUrl(botUrl).build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        return httpServiceProxyFactory.createClient(BotClient.class);
    }
}
