package edu.java.bot.client.scrapper;

import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface ScrapperClient {
    @GetExchange("/links")
    ListLinksResponse getTrackedLinks(Long tgChatId);

    @PostExchange("/links")
    LinkResponse addTrackingLink(
        @RequestHeader(name = "Tg-Chat-Id") Long chatId,
        @Valid @RequestBody AddLinkRequest request
    );

    @DeleteExchange("/links")
    LinkResponse deleteTrackingLink(
        @RequestHeader(name = "Tg-Chat-Id") Long chatId,
        @Valid @RequestBody RemoveLinkRequest request
    );

    @PostExchange("/tg-chat/{id}")
    void registerChat(@PathVariable Long id);

    @DeleteExchange
    void deleteChat(@PathVariable Long id);
}
