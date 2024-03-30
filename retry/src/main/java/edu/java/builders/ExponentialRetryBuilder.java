package edu.java.builders;

import edu.java.ErrorFilterPredicate;
import edu.java.RetryElement;
import java.util.function.Function;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public class ExponentialRetryBuilder implements Function<RetryElement, Retry> {
    @Override
    public Retry apply(RetryElement retryElement) {
        return RetryBackoffSpec.backoff(retryElement.maxAttempts(), retryElement.minDelay())
            .maxBackoff(retryElement.maxDelay())
            .filter(new ErrorFilterPredicate(retryElement.codes()));
    }
}
