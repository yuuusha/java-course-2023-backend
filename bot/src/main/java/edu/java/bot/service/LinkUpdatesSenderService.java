package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;

public interface LinkUpdatesSenderService {
    void sendLinkUpdate(LinkUpdate linkUpdate);
}
