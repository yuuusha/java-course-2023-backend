package edu.java;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "retry-query", ignoreUnknownFields = false)
public record RetryQueryConfiguration(List<RetryElement> retries) {
}
