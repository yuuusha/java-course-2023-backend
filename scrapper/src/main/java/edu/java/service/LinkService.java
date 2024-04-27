package edu.java.service;

import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URL;

public interface LinkService {

    ListLinksResponse getTrackedLinks(Long tgChatId);

    LinkResponse addTrackingLink(URL url, Long chatId);

    LinkResponse deleteTrackingLink(URL url, Long chatId);
}
