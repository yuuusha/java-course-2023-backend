package edu.java.service;

import edu.java.dto.Link;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListChatsResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {

    ListLinksResponse getTrackedLinks(Long tgChatId);

    LinkResponse addTrackingLink(URL url, Long chatId);

    LinkResponse deleteTrackingLink(URL url, Long chatId);

    List<Link> getOldLinks(Duration afterDuration, int limit);

    void update(Long id, OffsetDateTime lastUpdate);

    ListChatsResponse getLinkSubscribers(URL url);
}
