package edu.java.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Validated
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Scrapper API",
                                description = "Scrapper API", version = "1.0.0"))
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull Scheduler scheduler,
    String githubToken,
    AccessType databaseAccessType
) {
    public record Scheduler(@NotNull boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay,
                            int maxLinksPerCheck) {
    }

    public enum AccessType {
        JDBC,
        JPA,
        JOOQ
    }
}
