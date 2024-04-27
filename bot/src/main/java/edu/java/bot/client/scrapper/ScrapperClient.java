package edu.java.bot.client.scrapper;

import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface ScrapperClient {

    @GetExchange("/links")
    OptionalAnswer<ListLinksResponse> getTrackedLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId);

    @PostExchange("/links")
    OptionalAnswer<LinkResponse> addTrackingLink(
        @RequestHeader(name = "Tg-Chat-Id") Long tgChatId,
        @Valid @RequestBody AddLinkRequest request
    );

    @DeleteExchange("/links")
    OptionalAnswer<LinkResponse> deleteTrackingLink(
        @RequestHeader(name = "Tg-Chat-Id") Long tgChatId,
        @Valid @RequestBody RemoveLinkRequest request
    );

    @PostExchange("/tg-chat/{id}")
    OptionalAnswer<Void> registerChat(@PathVariable Long id);

    @DeleteExchange
    OptionalAnswer<Void> deleteChat(@PathVariable Long id);
}
