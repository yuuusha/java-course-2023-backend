package edu.java;

import java.util.List;
import java.util.function.Predicate;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ErrorFilterPredicate implements Predicate<Throwable> {
    private final List<Integer> retryCodes;

    public ErrorFilterPredicate(List<Integer> retryCodes) {
        this.retryCodes = retryCodes;
    }

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof WebClientResponseException e) {
            return retryCodes.contains(e.getStatusCode().value());
        }
        return true;
    }
}
