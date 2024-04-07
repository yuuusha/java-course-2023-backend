package edu.java.updates.sender;

import edu.java.client.bot.dto.request.LinkUpdate;

public interface LinkUpdateSender {
    void sendUpdate(LinkUpdate linkUpdate);
}
