package edu.java;

import edu.java.builders.ExponentialRetryBuilder;
import edu.java.builders.FixedRetryBuilder;
import edu.java.builders.LinearRetryBuilder;
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

    public static ExchangeFilterFunction createFilter(Retry retry) {
        return (response, next) -> next.exchange(response)
            .flatMap(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    return clientResponse.createError();
                } else {
                    return Mono.just(clientResponse);
                }
            }).retryWhen(retry);
    }

    public static Retry createRetry(RetryQueryConfiguration config, String target) {
        return config.retries().stream().filter(element -> element.target().equals(target)).findFirst()
            .map(element -> RETRY_BUILDERS.get(element.type()).apply(element))
            .orElseThrow(() -> new IllegalStateException("Unknown target " + target));
    }
}
