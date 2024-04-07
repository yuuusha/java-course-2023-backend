package edu.java.scrapper.supplier.stackoverflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.RetryQueryConfiguration;
import edu.java.configuration.supplier.StackOverflowConfig;
import edu.java.configuration.supplier.StackOverflowPatternConfig;
import edu.java.supplier.api.LinkInfo;
import edu.java.supplier.stackoverflow.StackOverflowInfoSupplier;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class StackOverflowInfoSupplierTest {
    private static WireMockServer server;

    private static final RetryQueryConfiguration RETRY_QUERY_CONFIGURATION = new RetryQueryConfiguration(
        List.of(new RetryQueryConfiguration.RetryElement(
                "stackoverflow",
                "fixed",
                1,
                1,
                Duration.ofSeconds(1),
                null,
                List.of(429)
            )
        )
    );

    @BeforeAll
    public static void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching("/questions/69228850.*"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "items": [
                          {
                            "title": "Spring Boot with postgres --&gt; HikariPool-1 - Exception during pool initialization",
                            "last_activity_date": 1631909213
                          }
                        ]
                    }
                    """)));
        server.stubFor(get(urlPathMatching("/questions/692288500000.*"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @SneakyThrows
    @Test
    public void returnRightInformationTest() {
        StackOverflowPatternConfig stackOverflowPatternConfig = Mockito.mock(StackOverflowPatternConfig.class);
        Mockito.when(stackOverflowPatternConfig.questions()).thenReturn("https://stackoverflow.com/questions/(\\d+).*");
        StackOverflowConfig config = new StackOverflowConfig(server.baseUrl(), stackOverflowPatternConfig);

        StackOverflowInfoSupplier supplier = new StackOverflowInfoSupplier(config, new ObjectMapper(), RETRY_QUERY_CONFIGURATION);
        LinkInfo info = supplier.fetchInfo(
            new URI(
                "https://stackoverflow.com/questions/69228850/spring-boot-with-postgres-hikaripool-1-exception-during-pool-initializatio").toURL()
        );
        Assertions.assertThat(info)
            .extracting(LinkInfo::url, LinkInfo::title)
            .contains(
                new URI(
                    "https://stackoverflow.com/questions/69228850/spring-boot-with-postgres-hikaripool-1-exception-during-pool-initializatio").toURL(),
                "Spring Boot with postgres --&gt; HikariPool-1 - Exception during pool initialization"
            );
    }

    @SneakyThrows
    @Test
    public void returnNullInformationWhenRepositoryWrongTest() {
        StackOverflowPatternConfig stackOverflowPatternConfig = Mockito.mock(StackOverflowPatternConfig.class);
        Mockito.when(stackOverflowPatternConfig.questions()).thenReturn("https://stackoverflow.com/wrongUrl/(\\d+).*");
        StackOverflowConfig config = new StackOverflowConfig(server.baseUrl(), stackOverflowPatternConfig);

        StackOverflowInfoSupplier supplier = new StackOverflowInfoSupplier(config, new ObjectMapper(), RETRY_QUERY_CONFIGURATION);
        LinkInfo info = supplier.fetchInfo(
            new URI("https://stackoverflow.com/questions/69228850/spring-boot-with-postgres-hikaripool-1-exception-during-pool-initializatio").toURL()
        );
        Assertions.assertThat(info).isNull();
    }
}
