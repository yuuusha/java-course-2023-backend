package edu.java.client.bot;

import edu.java.client.bot.dto.request.LinkUpdate;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface BotClient {
    @PostExchange
    void handleUpdate(@RequestBody @Valid LinkUpdate linkUpdate);
}
