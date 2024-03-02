package edu.java.contributor.sources.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowItem(String title, @JsonProperty("last_activity_date") OffsetDateTime lastModified) {
}
