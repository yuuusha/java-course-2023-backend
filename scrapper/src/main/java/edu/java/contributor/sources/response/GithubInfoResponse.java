package edu.java.contributor.sources.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GithubInfoResponse(
    @JsonProperty("full_name")
    String fullName,
    @JsonProperty("updated_at")
    OffsetDateTime lastModified,
    String description
) {
    public static final GithubInfoResponse
        EMPTY = new GithubInfoResponse(null, null, null);
}
