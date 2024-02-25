package edu.java.contributor.api;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class WebClientInfoContributor implements InfoContributor {

    protected WebClient webClient;

    public WebClientInfoContributor(WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClientInfoContributor(String apiUrl) {
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
