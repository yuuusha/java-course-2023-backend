package edu.java.configuration;

import edu.java.RetryFactory;
import edu.java.RetryQueryConfiguration;
import edu.java.client.bot.BotClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Configuration
public class BotClientConfiguration {

    @Value("${bot.url:http://localhost:8090}")
    private String botUrl;

    @Bean
    public BotClient botClient(RetryQueryConfiguration retryQueryConfiguration) {
        Retry retry = RetryFactory.createRetry(retryQueryConfiguration, "bot");
        WebClient webClient = WebClient.builder()
            .defaultStatusHandler(httpStatusCode -> true, clientResponse -> Mono.empty())
            .defaultHeader("Content-Type", "application/json")
            .filter(RetryFactory.createFilter(retry))
            .baseUrl(botUrl).build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        return httpServiceProxyFactory.createClient(BotClient.class);
    }
}
