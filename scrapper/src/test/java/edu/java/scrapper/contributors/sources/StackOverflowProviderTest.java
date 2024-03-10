package edu.java.scrapper.contributors.sources;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.contributor.api.Info;
import edu.java.contributor.sources.StackOverflowInfoProvider;
import java.net.URL;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class StackOverflowProviderTest {
    private static WireMockServer server;

    @BeforeAll
    public static void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching("/questions/5769717.*"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "items": [
                        {
                          "last_activity_date": 1620203807,
                          "creation_date": 1303637138,
                          "last_edit_date": 1613638309,
                          "question_id": 5769717,
                          "link": "https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string",
                          "title": "How can I get an HTTP response body as a string?"
                        }
                      ]
                    }""")));
        server.stubFor(get(urlPathMatching("/questions/1.*"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @SneakyThrows
    @Test
    public void getInfoCorrect() {
        StackOverflowInfoProvider provider = new StackOverflowInfoProvider(server.baseUrl());
        var info = provider.getInfo(new URL("https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string"));
        Assertions.assertThat(info)
            .extracting(Info::url, Info::title, Info::description)
            .contains(
                new URL("https://stackoverflow.com/questions/5769717/how-can-i-get-an-http-response-body-as-a-string"),
                "How can I get an HTTP response body as a string?",
                null
            );
    }

    @SneakyThrows
    @Test
    public void getInfoNotFound() {
        StackOverflowInfoProvider provider = new StackOverflowInfoProvider(server.baseUrl());
        var info = provider.getInfo(new URL("https://stackoverflow.com/questions/1"));
        Assertions.assertThat(info).isEqualTo(new Info(null, null, null, null));
    }

    @SneakyThrows
    @Test
    public void isSupportedTrue() {
        StackOverflowInfoProvider provider = new StackOverflowInfoProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://stackoverflow.com/questions/5769717"));
        Assertions.assertThat(info).isTrue();
    }

    @SneakyThrows
    @Test
    public void isSupportedFalse() {
        StackOverflowInfoProvider provider = new StackOverflowInfoProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://google.com/err/err"));
        Assertions.assertThat(info).isFalse();
    }
}
