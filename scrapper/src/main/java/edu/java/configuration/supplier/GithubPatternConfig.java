package edu.java.configuration.supplier;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

@Validated
public record GithubPatternConfig(@NotEmpty String repository) {
}
