package edu.java.supplier.stackoverflow.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowItem(
    String title,
    @JsonProperty("last_activity_date") OffsetDateTime lastUpdate,
    @JsonProperty("answer_count") int answerCount,
    int score
) {
}
