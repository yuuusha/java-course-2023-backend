package edu.java.scrapper.service;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.TelegramChatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcTelegramChatServiceTest extends IntegrationEnvironment {

    @Autowired
    private TelegramChatService chatService;
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void registerChatTest() {
        chatService.registerChat(41L);
        Assertions.assertThat(chatRepository.isExists(41L)).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void registerChatAlreadyExistTest() {
        chatService.registerChat(41L);
        Assertions.assertThatThrownBy(() -> chatService.registerChat(41L))
            .isInstanceOf(ChatAlreadyRegisteredException.class);
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {
        chatService.registerChat(41L);
        chatService.deleteChat(41L);
        Assertions.assertThat(chatRepository.isExists(41L)).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatNotFoundTest() {
        Assertions.assertThatThrownBy(() -> chatService.deleteChat(41L))
            .isInstanceOf(ChatNotFoundException.class);
    }
}
