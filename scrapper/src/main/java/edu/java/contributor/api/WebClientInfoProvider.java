package edu.java.contributor.api;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class WebClientInfoProvider implements InfoProvider {

    protected WebClient webClient;

    public WebClientInfoProvider(WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClientInfoProvider(String apiUrl) {
        this(WebClient.create(apiUrl));
    }

    protected <T> T executeRequest(String uri, Class<T> type, T defaultValue) {
        return webClient
            .get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(type)
            .onErrorReturn(defaultValue)
            .block();
    }
}
