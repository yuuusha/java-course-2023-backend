package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DefaultLinkUpdatesSenderService implements LinkUpdatesSenderService {
    @Override
    public void sendLinkUpdate(LinkUpdate linkUpdate) {
        linkUpdate.tgChatIds().forEach(chatId -> log.info("{} обновлено {}", chatId, linkUpdate.url()));
    }
}
