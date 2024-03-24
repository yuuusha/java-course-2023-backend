package edu.java.bot.service;

import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;

public interface BotService {

    OptionalAnswer<Void> registerUserIfNew(Long id);

    OptionalAnswer<Void> deleteUser(Long id);

    OptionalAnswer<LinkResponse> trackUserLink(Long id, String url);

    OptionalAnswer<LinkResponse> unTrackUserLink(Long id, String url);

    OptionalAnswer<ListLinksResponse> userLinks(Long id);
}
