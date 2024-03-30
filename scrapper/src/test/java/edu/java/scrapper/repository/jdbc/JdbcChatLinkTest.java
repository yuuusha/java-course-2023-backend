package edu.java.scrapper.repository.jdbc;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.util.URLCreator;
import java.time.OffsetDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatLinkTest extends IntegrationEnvironment {

    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private JdbcChatLinkRepository jdbcChatLinkRepository;

    private Link link;

    @BeforeEach
    void setUp() {
        link = new Link(0L, URLCreator.createURL("https://google.com"), OffsetDateTime.MIN, OffsetDateTime.MAX, "");
    }

    @Test
    @Transactional
    @Rollback
    void findAllLinkByChatIdTest() {
        jdbcChatRepository.add(41L);
        Long linkId = jdbcLinkRepository.add(link);
        jdbcChatLinkRepository.add(41L, linkId);
        Assertions.assertThat(jdbcChatLinkRepository.findAllLinkByChatId(41L)).contains(
            link = new Link(
                linkId, URLCreator.createURL("https://google.com"), OffsetDateTime.MIN, OffsetDateTime.MAX, ""
            )
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatByLinkIdTest() {
        jdbcChatRepository.add(41L);
        Long linkId = jdbcLinkRepository.add(link);
        jdbcChatLinkRepository.add(41L, linkId);
        Assertions.assertThat(jdbcChatLinkRepository.findAllChatByLinkId(linkId)).contains(new Chat(41L));
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkFromDatabaseTest() {
        jdbcChatRepository.add(41L);
        Long linkId = jdbcLinkRepository.add(link);
        jdbcChatLinkRepository.add(41L, linkId);
        jdbcChatLinkRepository.remove(41L, linkId);
        Assertions.assertThat(jdbcChatLinkRepository.findAllLinkByChatId(41L)).doesNotContain(
            link =
                new Link(linkId, URLCreator.createURL("https://google.com"), OffsetDateTime.MIN, OffsetDateTime.MAX, "")
        );
    }
}
