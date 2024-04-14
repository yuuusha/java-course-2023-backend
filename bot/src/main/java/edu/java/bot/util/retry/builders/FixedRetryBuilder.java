package edu.java.bot.util.retry.builders;

import edu.java.bot.configuration.RetryQueryConfiguration;
import edu.java.bot.util.retry.ErrorFilterPredicate;
import java.util.function.Function;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public class FixedRetryBuilder implements Function<RetryQueryConfiguration.RetryElement, Retry> {
    @Override
    public Retry apply(RetryQueryConfiguration.RetryElement retryElement) {
        return RetryBackoffSpec.fixedDelay(retryElement.maxAttempts(), retryElement.minDelay())
            .filter(new ErrorFilterPredicate(retryElement.codes()));
    }
}
