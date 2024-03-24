package edu.java.supplier.api;

import java.net.URL;
import java.time.OffsetDateTime;

public interface InfoSupplier {

    LinkInfo fetchInfo(URL url);

    boolean isSupported(URL url);

    String getTypeSupplier();

    LinkInfo filterByDateTime(LinkInfo linkInfo, OffsetDateTime afterDateTime, String metaInfo);
}
