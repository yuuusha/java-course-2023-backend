package edu.java.bot.service;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.java.bot.service.URLService.createURL;

@Service
@RequiredArgsConstructor
public class DefaultBotService implements BotService {

    private final ScrapperClient scrapperClient;

    @Override
    public boolean isUserRegistered(Long id) {
        // Заглушка
        return false;
    }

    @Override
    public boolean isLinkExist(Long id, String link) {
        // Заглушка
        return false;
    }

    @Override
    public void registerUser(Long id) {
        scrapperClient.registerChat(id);
    }

    @Override
    public void deleteUser(Long id) {
        scrapperClient.deleteChat(id);
    }

    @Override
    public LinkResponse addLink(Long id, String link) {
        return scrapperClient.addTrackingLink(id, new AddLinkRequest(createURL(link)));
    }

    @Override
    public LinkResponse removeLink(Long id, String link) {
        return scrapperClient.deleteTrackingLink(id, new RemoveLinkRequest(createURL(link)));
    }

    @Override
    public ListLinksResponse getListLinks(Long id) {
        return scrapperClient.getTrackedLinks(id);
    }
}
