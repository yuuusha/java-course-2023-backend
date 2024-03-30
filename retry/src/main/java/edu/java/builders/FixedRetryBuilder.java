package edu.java.builders;

import edu.java.ErrorFilterPredicate;
import edu.java.RetryElement;
import java.util.function.Function;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public class FixedRetryBuilder implements Function<RetryElement, Retry> {
    @Override
    public Retry apply(RetryElement retryElement) {
        return RetryBackoffSpec.fixedDelay(retryElement.maxAttempts(), retryElement.minDelay())
            .filter(new ErrorFilterPredicate(retryElement.codes()));
    }
}
