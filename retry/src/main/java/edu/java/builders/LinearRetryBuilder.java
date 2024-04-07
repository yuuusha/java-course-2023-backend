package edu.java.builders;

import edu.java.ErrorFilterPredicate;
import edu.java.LinearRetryBackoffSpec;
import edu.java.RetryQueryConfiguration;
import java.util.function.Function;
import reactor.util.retry.Retry;

public class LinearRetryBuilder implements Function<RetryQueryConfiguration.RetryElement, Retry> {
    @Override
    public Retry apply(RetryQueryConfiguration.RetryElement retryElement) {
        return LinearRetryBackoffSpec.linear(retryElement.maxAttempts(), retryElement.minDelay())
            .factor(retryElement.factor()).filter(new ErrorFilterPredicate(retryElement.codes()));
    }
}
