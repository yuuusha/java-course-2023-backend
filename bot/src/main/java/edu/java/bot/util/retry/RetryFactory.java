package edu.java.bot.util.retry;

import edu.java.bot.configuration.RetryQueryConfiguration;
import edu.java.bot.util.retry.builders.ExponentialRetryBuilder;
import edu.java.bot.util.retry.builders.FixedRetryBuilder;
import edu.java.bot.util.retry.builders.LinearRetryBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@UtilityClass
public class RetryFactory {

    private static final Map<String, Function<RetryQueryConfiguration.RetryElement, Retry>> RETRY_BUILDERS =
        new HashMap<>();

    static {
        RETRY_BUILDERS.put("fixed", new FixedRetryBuilder());
        RETRY_BUILDERS.put("linear", new LinearRetryBuilder());
        RETRY_BUILDERS.put("exponential", new ExponentialRetryBuilder());
    }

    public static ExchangeFilterFunction createFilter(RetryQueryConfiguration config, String target) {
        return (response, next) -> next.exchange(response)
            .flatMap(clientResponse -> {
                if (clientResponse.statusCode().isError()
                    && config.targets().get(target).codes().contains(clientResponse.statusCode().value())) {
                    return clientResponse.createError();
                } else {
                    return Mono.just(clientResponse);
                }
            }).retryWhen(createRetry(config, target));
    }

    public static Retry createRetry(RetryQueryConfiguration config, String target) {
        RetryQueryConfiguration.RetryElement element = config.targets().get(target);
        return RETRY_BUILDERS.get(element.type()).apply(element);
    }
}
