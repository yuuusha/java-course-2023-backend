package edu.java.bot.client.scrapper.dto.response;

import java.net.URL;

public record LinkResponse(
    Long id,
    URL url
) {
}
