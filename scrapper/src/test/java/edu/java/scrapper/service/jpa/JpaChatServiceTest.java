package edu.java.scrapper.service.jpa;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.jpa.JpaChatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaChatServiceTest extends IntegrationEnvironment {

    @Autowired
    private JpaChatService chatService;

    @Autowired
    private JpaChatRepository chatRepository;


    @Test
    @Transactional
    @Rollback
    void registerChatTest() {
        chatService.registerChat(41L);
        Assertions.assertThat(chatRepository.findById(41L).isPresent()).isTrue();
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
        Assertions.assertThat(chatRepository.findById(41L).isPresent()).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatNotFoundTest() {
        Assertions.assertThatThrownBy(() -> chatService.deleteChat(41L))
            .isInstanceOf(ChatNotFoundException.class);
    }

    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }
}
