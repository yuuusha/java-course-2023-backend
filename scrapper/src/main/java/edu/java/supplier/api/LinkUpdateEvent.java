package edu.java.supplier.api;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Builder;

@Builder
public record LinkUpdateEvent(
    String typeEvent,
    OffsetDateTime lastUpdate,
    Map<String, String> eventData
) {
}
