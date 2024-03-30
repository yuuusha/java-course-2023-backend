package edu.java;

import java.time.Duration;
import java.util.List;
import org.jetbrains.annotations.NotNull;

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
