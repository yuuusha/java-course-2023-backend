package edu.java.scrapper.repository.jooq;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.repository.jooq.JooqChatLinkRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
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
public class JooqChatLinkTest extends IntegrationEnvironment {

    private static final OffsetDateTime MIN =
        OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());

    private static final OffsetDateTime MAX =
        OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());


    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @Autowired
    private JooqChatLinkRepository jooqChatLinkRepository;

    private Link link;

    @BeforeEach
    void setUp() {
        link = new Link(0L, URLCreator.createURL("https://google.com"), MIN, MAX, "");
    }

    @Test
    @Transactional
    @Rollback
    void findAllLinkByChatIdTest() {
        jooqChatRepository.add(41L);
        Long linkId = jooqLinkRepository.add(link);
        jooqChatLinkRepository.add(41L, linkId);
        Assertions.assertThat(jooqChatLinkRepository.findAllLinkByChatId(41L)).contains(
            link = new Link(
                linkId, URLCreator.createURL("https://google.com"), MIN, MAX, ""
            )
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatByLinkIdTest() {
        jooqChatRepository.add(41L);
        Long linkId = jooqLinkRepository.add(link);
        jooqChatLinkRepository.add(41L, linkId);
        Assertions.assertThat(jooqChatLinkRepository.findAllChatByLinkId(linkId)).contains(new Chat(41L));
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkFromDatabaseTest() {
        jooqChatRepository.add(41L);
        Long linkId = jooqLinkRepository.add(link);
        jooqChatLinkRepository.add(41L, linkId);
        jooqChatLinkRepository.remove(41L, linkId);
        Assertions.assertThat(jooqChatLinkRepository.findAllLinkByChatId(41L)).doesNotContain(
            link =
                new Link(linkId, URLCreator.createURL("https://google.com"), MIN, MAX, "")
        );
    }
}
