package edu.java.bot.service;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.java.bot.service.URLService.createURL;

@Service
@RequiredArgsConstructor
public class DefaultBotService implements BotService {

    private final ScrapperClient scrapperClient;

    @Override
    public OptionalAnswer<Void> registerUserIfNew(Long id) {
        return scrapperClient.registerChat(id);
    }

    @Override
    public OptionalAnswer<Void> deleteUser(Long id) {
        return scrapperClient.deleteChat(id);
    }

    @Override
    public OptionalAnswer<LinkResponse> trackUserLink(Long id, String link) {
        return scrapperClient.addTrackingLink(id, new AddLinkRequest(createURL(link)));
    }

    @Override
    public OptionalAnswer<LinkResponse> unTrackUserLink(Long id, String link) {
        return scrapperClient.deleteTrackingLink(id, new RemoveLinkRequest(createURL(link)));
    }

    @Override
    public OptionalAnswer<ListLinksResponse> userLinks(Long id) {
        return scrapperClient.getTrackedLinks(id);
    }
}
