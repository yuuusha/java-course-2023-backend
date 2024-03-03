package edu.java.bot.service;

import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;

public interface BotService {
    boolean isUserRegistered(Long id);

    void registerUser(Long id);

    void deleteUser(Long id);

    boolean isLinkExist(Long id, String url);

    LinkResponse addLink(Long id, String url);

    LinkResponse removeLink(Long id, String url);

    ListLinksResponse getListLinks(Long id);
}
