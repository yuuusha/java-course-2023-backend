package edu.java.supplier.api;

import java.net.URL;
import java.util.List;

public record LinkInfo(URL url, String title, List<LinkUpdateEvent> events, String metaInfo) {
}
