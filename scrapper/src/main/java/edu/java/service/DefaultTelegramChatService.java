package edu.java.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DefaultTelegramChatService implements TelegramChatService {
    @Override
    public void registerChat(Long chatId) {
        log.info("Registered chat {}", chatId);
    }

    @Override
    public void deleteChat(Long chatId) {
        log.info("Deleted chat {}", chatId);
    }
}
