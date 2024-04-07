package edu.java;

import java.time.Duration;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry-query", ignoreUnknownFields = false)
public record RetryQueryConfiguration(List<RetryElement> retries) {
    public record RetryElement(
        @NotNull String target,
        @NotNull String type,
        int maxAttempts,
        double factor,
        Duration minDelay,
        Duration maxDelay,
        List<Integer> codes
    ) {
    }
}
