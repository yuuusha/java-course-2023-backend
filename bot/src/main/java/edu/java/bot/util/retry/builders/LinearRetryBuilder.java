package edu.java.bot.util.retry.builders;

import edu.java.bot.configuration.RetryQueryConfiguration;
import edu.java.bot.util.retry.ErrorFilterPredicate;
import edu.java.bot.util.retry.LinearRetryBackoffSpec;
import java.util.function.Function;
import reactor.util.retry.Retry;

public class LinearRetryBuilder implements Function<RetryQueryConfiguration.RetryElement, Retry> {
    @Override
    public Retry apply(RetryQueryConfiguration.RetryElement retryElement) {
        return LinearRetryBackoffSpec.linear(retryElement.maxAttempts(), retryElement.minDelay())
            .factor(retryElement.factor()).filter(new ErrorFilterPredicate(retryElement.codes()));
    }
}
