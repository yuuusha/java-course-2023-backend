package edu.java.scrapper.repository.jooq;

import edu.java.dto.Chat;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqChatTest extends IntegrationEnvironment {

    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        jooqChatRepository.add(41L);
        Assertions.assertThat(jooqChatRepository.findAll()).contains(new Chat(41L));
    }

    @Test
    @Transactional
    @Rollback
    void addAndDeleteChatTest() {
        jooqChatRepository.add(41L);
        jooqChatRepository.remove(41L);
        Assertions.assertThat(jooqChatRepository.findAll()).doesNotContain(new Chat(41L));
    }

}
