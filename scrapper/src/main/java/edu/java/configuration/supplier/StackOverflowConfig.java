package edu.java.configuration.supplier;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "supplier.stackoverflow", ignoreUnknownFields = false)
public record StackOverflowConfig(@NotEmpty String url, StackOverflowPatternConfig patterns) {
}
