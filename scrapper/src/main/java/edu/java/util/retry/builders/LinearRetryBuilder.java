package edu.java.util.retry.builders;

import edu.java.configuration.RetryQueryConfiguration;
import edu.java.util.retry.ErrorFilterPredicate;
import edu.java.util.retry.LinearRetryBackoffSpec;
import java.util.function.Function;
import reactor.util.retry.Retry;

public class LinearRetryBuilder implements Function<RetryQueryConfiguration.RetryElement, Retry> {
    @Override
    public Retry apply(RetryQueryConfiguration.RetryElement retryElement) {
        return LinearRetryBackoffSpec.linear(retryElement.maxAttempts(), retryElement.minDelay())
            .factor(retryElement.factor()).filter(new ErrorFilterPredicate(retryElement.codes()));
    }
}
