package edu.java.dto;

import java.net.URL;
import java.time.OffsetDateTime;

public record Link(Long linkId, URL url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {
}
