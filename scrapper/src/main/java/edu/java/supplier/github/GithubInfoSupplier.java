package edu.java.supplier.github;

import edu.java.RetryFactory;
import edu.java.RetryQueryConfiguration;
import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.supplier.GithubConfig;
import edu.java.supplier.api.LinkInfo;
import edu.java.supplier.api.LinkUpdateEvent;
import edu.java.supplier.api.WebClientInfoSupplier;
import edu.java.supplier.github.data.GithubEventsCollector;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Component
public class GithubInfoSupplier extends WebClientInfoSupplier {

    private static final int MAX_PER_UPDATE = 10;

    private static final String TYPE_SUPPLIER = "github";

    private final GithubEventResolver eventResolver;

    private final Pattern repositoryPattern;

    @Autowired
    public GithubInfoSupplier(
        GithubConfig githubConfig,
        ApplicationConfig applicationConfig,
        RetryQueryConfiguration retryQueryConfiguration
    ) {
        super(WebClient.builder()
            .baseUrl(githubConfig.url())
            .defaultHeaders(headers -> {
                if (applicationConfig.githubToken() != null) {
                    headers.set("Authorization", "Bearer " + applicationConfig.githubToken());
                }
            })
            .filter(RetryFactory.createFilter(RetryFactory.createRetry(retryQueryConfiguration, TYPE_SUPPLIER)))
            .build()
        );
        repositoryPattern = Pattern.compile(githubConfig.patterns().repository());
        eventResolver = new GithubEventResolver();
    }

    public String getTypeSupplier() {
        return TYPE_SUPPLIER;
    }

    @Override
    public LinkInfo fetchInfo(URL url) {
        GithubEventsCollector eventsCollector = null;
        if (isSupported(url)) {
            eventsCollector = executeRequestGet(
                "repos" + url.getPath() + "/events?per_page=" + MAX_PER_UPDATE,
                GithubEventsCollector.class,
                GithubEventsCollector.EMPTY
            );
        }
        if (eventsCollector == null || eventsCollector.equals(GithubEventsCollector.EMPTY)
            || eventsCollector.events().isEmpty()) {
            return null;
        }

        String repositoryName = eventsCollector.events().getFirst().repo().name();

        List<LinkUpdateEvent> linkUpdateEvents = eventsCollector.events().stream().map(it -> {
            var converter = eventResolver.getConverter(it.type());
            if (converter == null) {
                return new LinkUpdateEvent(
                    "github." + it.type().toLowerCase(),
                    it.lastModified(),
                    Map.of("type", it.type())
                );
            }
            return eventResolver.getConverter(it.type()).apply(it);
        }).toList();

        return new LinkInfo(url, repositoryName, linkUpdateEvents, "");
    }

    @Override
    public boolean isSupported(URL url) {
        Pattern pattern = supportedPattern(url);
        return pattern != null;
    }

    private Pattern supportedPattern(URL url) {
        if (repositoryPattern.matcher(url.toString()).matches()) {
            return repositoryPattern;
        }
        return null;
    }

    @Override
    public LinkInfo filterByDateTime(LinkInfo linkInfo, OffsetDateTime afterDateTime, String context) {
        List<LinkUpdateEvent> filteredUpdates =
            linkInfo.events().stream().filter(event -> event.lastUpdate().isAfter(afterDateTime)).toList();
        return new LinkInfo(
            linkInfo.url(),
            linkInfo.title(),
            filteredUpdates,
            ""
        );
    }
}
