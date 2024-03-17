package edu.java.dto;

import java.net.URL;
import java.time.OffsetDateTime;

public record Link(Long linkId, URL url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck, String metaInfo) {
    public Link(Long linkId, URL url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {
        this(linkId, url, lastUpdate, lastCheck, null);
    }

    public Link(Long linkId, URL url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck, String metaInfo) {
        this.linkId = linkId;
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.lastCheck = lastCheck;
        this.metaInfo = metaInfo;
    }
}
