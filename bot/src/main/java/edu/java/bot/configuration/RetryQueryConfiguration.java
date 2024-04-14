package edu.java.bot.configuration;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry-query", ignoreUnknownFields = false)
public record RetryQueryConfiguration(Map<String, RetryElement> targets) {
    public record RetryElement(
        @NotNull String type,
        int maxAttempts,
        double factor,
        Duration minDelay,
        Duration maxDelay,
        List<Integer> codes
    ) {
    }
}
