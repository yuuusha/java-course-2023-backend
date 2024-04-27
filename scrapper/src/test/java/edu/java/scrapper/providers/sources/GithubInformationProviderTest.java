package edu.java.scrapper.providers.sources;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.provider.api.Info;
import edu.java.provider.sources.GithubInfoProvider;
import java.net.URL;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class GithubInformationProviderTest {

    private static WireMockServer server;

    @BeforeAll
    public static void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching("/repos/yuuusha/java-course-2023"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "id": 92097716,
                      "full_name": "yuuusha/java-course-2023",
                      "created_at": "2023-10-08T14:47:38Z",
                      "updated_at": "2024-02-13T16:07:35Z",
                      "pushed_at": "2024-01-31T11:47:13Z",
                      "language": "Java",
                      "description": "null"
                    }""")));
        server.stubFor(get(urlPathMatching("/repos/err/err"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @SneakyThrows
    @Test
    public void getInfoCorrect() {
        GithubInfoProvider contributor = new GithubInfoProvider(server.baseUrl());
        var info = contributor.getInfo(new URL("https://github.com/yuuusha/java-course-2023"));
        Assertions.assertThat(info)
            .extracting(Info::url, Info::title, Info::description)
            .contains(
                new URL("https://github.com/yuuusha/java-course-2023"),
                "yuuusha/java-course-2023",
                "null"
            );
    }

    @SneakyThrows
    @Test
    public void getInfoNotFound() {
        GithubInfoProvider provider = new GithubInfoProvider(server.baseUrl());
        var info = provider.getInfo(new URL("https://github.com/err/err"));
        Assertions.assertThat(info).isEqualTo(new Info(null, null, null, null));
    }

    @SneakyThrows
    @Test
    public void isSupportedTrue() {
        GithubInfoProvider provider = new GithubInfoProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://github.com/err/err"));
        Assertions.assertThat(info).isTrue();
    }

    @SneakyThrows
    @Test
    public void isSupportedFalse() {
        GithubInfoProvider provider = new GithubInfoProvider(server.baseUrl());
        var info = provider.isSupported(new URL("https://gitlab.com/err/err"));
        Assertions.assertThat(info).isFalse();
    }

}
