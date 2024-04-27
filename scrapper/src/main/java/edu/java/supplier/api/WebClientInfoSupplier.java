package edu.java.supplier.api;

import java.time.OffsetDateTime;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class WebClientInfoSupplier implements InfoSupplier {
    private final WebClient webClient;

    public WebClientInfoSupplier(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public WebClientInfoSupplier(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> T executeRequestGet(String uri, Class<T> type, T defaultValue) {
        return webClient.get().uri(uri).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(type)
            .onErrorReturn(defaultValue).block();
    }

    public abstract LinkInfo filterByDateTime(LinkInfo linkInfo, OffsetDateTime afterDateTime, String context);
}
