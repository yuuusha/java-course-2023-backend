package edu.java.supplier.github.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.java.supplier.github.util.GithubEventsCollectorDeserializer;
import java.util.List;

@JsonDeserialize(using = GithubEventsCollectorDeserializer.class)
public record GithubEventsCollector(
    List<GithubEventInfo> events
) {
    public static final GithubEventsCollector EMPTY = new GithubEventsCollector(null);
}
