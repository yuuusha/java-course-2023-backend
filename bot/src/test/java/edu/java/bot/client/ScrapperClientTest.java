package edu.java.bot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import java.net.URI;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

@WireMockTest(httpPort = 8081)
public class ScrapperClientTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Test
    public void addLinkTest() {
        stubFor(
            post(urlPathMatching("/links"))
                .withHeader("Tg-Chat-Id", equalTo("42"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(new LinkResponse(
                        42L,
                        URI.create("https://google.com").toURL()
                    )))
                )
        );

        ScrapperClient scrapperClient = scrapperClient("http://localhost:8081");
        LinkResponse response =
            scrapperClient.addTrackingLink(42L, new AddLinkRequest(URI.create("https://google.com").toURL())).answer();
        Assertions.assertThat(response)
            .extracting(LinkResponse::id, LinkResponse::url)
            .contains(42L, URI.create("https://google.com").toURL());
    }

    @SneakyThrows
    @Test
    public void deleteLinkTest() {
        stubFor(
            delete(urlPathMatching("/links"))
                .withHeader("Tg-Chat-Id", equalTo("42"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(new LinkResponse(
                        42L,
                        URI.create("https://google.com").toURL()
                    )))
                )
        );

        ScrapperClient scrapperClient = scrapperClient("http://localhost:8081");
        LinkResponse response =
            scrapperClient.deleteTrackingLink(42L, new RemoveLinkRequest(URI.create("https://google.com").toURL())).answer();
        Assertions.assertThat(response)
            .extracting(LinkResponse::id, LinkResponse::url)
            .contains(42L, URI.create("https://google.com").toURL());
    }

    private static ScrapperClient scrapperClient(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .defaultStatusHandler(httpStatusCode -> true, clientResponse -> Mono.empty())
            .baseUrl(baseUrl).build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        return httpServiceProxyFactory.createClient(ScrapperClient.class);
    }

}
