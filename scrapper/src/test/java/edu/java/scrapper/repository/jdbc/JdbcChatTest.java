package edu.java.scrapper.repository.jdbc;

import edu.java.dto.Chat;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatTest extends IntegrationEnvironment {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        jdbcChatRepository.add(41L);
        Assertions.assertThat(jdbcChatRepository.findAll()).contains(new Chat(41L));
    }

    @Test
    @Transactional
    @Rollback
    void addAndDeleteChatTest() {
        jdbcChatRepository.add(41L);
        jdbcChatRepository.remove(41L);
        Assertions.assertThat(jdbcChatRepository.findAll()).doesNotContain(new Chat(41L));
    }

}
