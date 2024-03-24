package edu.java.supplier.github;

import edu.java.supplier.api.EventResolver;
import edu.java.supplier.api.LinkUpdateEvent;
import edu.java.supplier.github.data.GithubEventInfo;
import java.util.Map;

@SuppressWarnings("MultipleStringLiterals")
public class GithubEventResolver extends EventResolver<GithubEventInfo> {

    public GithubEventResolver() {
        registerConverter(
            "PushEvent",
            item -> new LinkUpdateEvent(
                "github.push_event",
                item.lastModified(),
                Map.of("size", String.valueOf(item.payload().size()))
            )
        );
        registerConverter(
            "IssuesEvent",
            item -> new LinkUpdateEvent(
                "github.issues_event",
                item.lastModified(),
                Map.of("title", item.payload().issue().title())
            )
        );
        registerConverter(
            "PullRequestEvent",
            item -> new LinkUpdateEvent(
                "github.pull_request_event",
                item.lastModified(),
                Map.of("title", item.payload().pullRequest().title())
            )
        );
        registerConverter(
            "CreateEvent",
            item -> new LinkUpdateEvent(
                "github.create_event",
                item.lastModified(),
                Map.of(
                    "ref", String.valueOf(item.payload().ref()),
                    "ref_type", String.valueOf(item.payload().refType())
                )
            )
        );
    }
}
