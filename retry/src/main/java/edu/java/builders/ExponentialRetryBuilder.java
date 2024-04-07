package edu.java.builders;

import edu.java.ErrorFilterPredicate;
import edu.java.RetryQueryConfiguration;
import java.util.function.Function;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public class ExponentialRetryBuilder implements Function<RetryQueryConfiguration.RetryElement, Retry> {
    @Override
    public Retry apply(RetryQueryConfiguration.RetryElement retryElement) {
        return RetryBackoffSpec.backoff(retryElement.maxAttempts(), retryElement.minDelay())
            .maxBackoff(retryElement.maxDelay())
            .filter(new ErrorFilterPredicate(retryElement.codes()));
    }
}
