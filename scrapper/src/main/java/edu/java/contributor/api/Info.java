package edu.java.contributor.api;

import java.net.URL;
import java.time.OffsetDateTime;

public record Info(
    URL url,
    String title,
    String description,
    OffsetDateTime lastModified
) {
}
