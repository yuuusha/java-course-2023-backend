package edu.java.supplier.stackoverflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.RetryFactory;
import edu.java.RetryQueryConfiguration;
import edu.java.configuration.supplier.StackOverflowConfig;
import edu.java.supplier.api.EventResolver;
import edu.java.supplier.api.LinkInfo;
import edu.java.supplier.api.LinkUpdateEvent;
import edu.java.supplier.api.WebClientInfoSupplier;
import edu.java.supplier.stackoverflow.data.StackOverflowInfoResponse;
import edu.java.supplier.stackoverflow.data.StackOverflowItem;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Getter
@Component
public class StackOverflowInfoSupplier extends WebClientInfoSupplier {

    private static final String TYPE_SUPPLIER = "stackoverflow";

    private static final TypeReference<HashMap<String, String>> STRING_STRING_HASHMAP = new TypeReference<>() {
    };

    private final Pattern questionsPattern;

    private final EventResolver<StackOverflowItem> eventResolver;

    private final ObjectMapper mapper;

    public StackOverflowInfoSupplier(
        StackOverflowConfig config,
        ObjectMapper mapper,
        RetryQueryConfiguration retryQueryConfiguration
    ) {
        super(config.url(), RetryFactory.createRetry(retryQueryConfiguration, TYPE_SUPPLIER));
        questionsPattern = Pattern.compile(config.patterns().questions());
        this.mapper = mapper;
        eventResolver = new StackOverflowEventResolver();
    }

    public String getTypeSupplier() {
        return TYPE_SUPPLIER;
    }

    @SneakyThrows @Override
    public LinkInfo fetchInfo(URL url) {
        Pattern pattern = supportedPattern(url);
        if (pattern == null) {
            return null;
        }

        StackOverflowInfoResponse eventsCollector;
        Matcher matcher = pattern.matcher(url.toString());
        if (matcher.find()) {
            String questionId = matcher.group(1);

            if (pattern == questionsPattern) {
                eventsCollector = executeRequestGet(
                    "/questions/" + questionId + "?site=stackoverflow",
                    StackOverflowInfoResponse.class,
                    StackOverflowInfoResponse.EMPTY
                );
            } else {
                eventsCollector = null;
            }
        } else {
            eventsCollector = null;
        }

        if (eventsCollector == null || eventsCollector.equals(StackOverflowInfoResponse.EMPTY)
            || eventsCollector.items().length == 0) {
            return null;
        }

        List<LinkUpdateEvent> events = eventResolver.getEventMapConverters().values().stream()
            .map(eventFunction ->
                eventFunction.apply(eventsCollector.items()[0]))
            .toList();

        HashMap<String, String> metaInformation = new HashMap<>();

        for (LinkUpdateEvent event : events) {
            metaInformation.putAll(event.eventData());
        }

        return new LinkInfo(
            url,
            eventsCollector.items()[0].title(),
            events,
            mapper.writeValueAsString(metaInformation)
        );
    }

    @Override
    public boolean isSupported(URL url) {
        Pattern pattern = supportedPattern(url);
        return pattern != null;
    }

    private Pattern supportedPattern(URL url) {
        if (questionsPattern.matcher(url.toString()).matches()) {
            return questionsPattern;
        }
        return null;
    }

    @SneakyThrows
    @Override
    public LinkInfo filterByDateTime(LinkInfo linkInfo, OffsetDateTime afterDateTime, String metaInfo) {
        Map<String, String> data = new HashMap<>();
        if (metaInfo != null && !metaInfo.isEmpty()) {
            data = mapper.readValue(metaInfo, STRING_STRING_HASHMAP);
        }
        List<LinkUpdateEvent> events = new ArrayList<>();
        List<LinkUpdateEvent> filteredEvents =
            linkInfo.events()
                .stream()
                .filter(event -> event.lastUpdate().isAfter(afterDateTime))
                .toList();
        for (LinkUpdateEvent event : filteredEvents) {
            for (Map.Entry<String, String> entry : event.eventData().entrySet()) {
                if (data.containsKey(entry.getKey())) {
                    String value = data.get(entry.getKey());
                    if (value.equals(entry.getValue())) {
                        continue;
                    }
                }
                data.put(entry.getKey(), entry.getValue());
                if (!events.contains(event)) {
                    events.add(event);
                }
            }
        }
        return new LinkInfo(linkInfo.url(), linkInfo.title(), events, mapper.writeValueAsString(data));
    }
}
