package edu.java.scrapper.repository.jooq;

import edu.java.dto.Link;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.util.URLCreator;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqLinkTest extends IntegrationEnvironment {

    private static final OffsetDateTime MIN =
            OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());

    private static final OffsetDateTime MAX =
        OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset());

    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    private Link link;
    private Link link2;

    @BeforeEach
    void setUp() {
        link = new Link(0L, URLCreator.createURL("https://google.com"), MIN, MAX, "");
        link2 = new Link(0L, URLCreator.createURL("https://tinkoff.ru"), MIN, MAX, "");
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        Long id = jooqLinkRepository.add(link);
        Optional<Link> linkFromDatabase = jooqLinkRepository.findById(id);
        Assertions.assertThat(linkFromDatabase.get().url()).isEqualTo(link.url());
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkTest() {
        jooqLinkRepository.add(link);
        Optional<Link> linkFromDatabase = jooqLinkRepository.findByUrl(link.url());
        jooqLinkRepository.remove(linkFromDatabase.get().linkId());
        Optional<Link> actualResult = jooqLinkRepository.findByUrl(link.url());
        Assertions.assertThat(actualResult).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinks() {
        jooqLinkRepository.add(link);
        jooqLinkRepository.add(link2);
        List<Link> linksFromDatabase = jooqLinkRepository.findAll();
        Assertions.assertThat(linksFromDatabase).map(Link::url).contains(link.url(), link2.url());
    }

    @Test
    @Transactional
    @Rollback
    void oldLinksTest() {
        link = new Link(0L, URLCreator.createURL("https://google.com"), MIN, OffsetDateTime.now(), "");
        link2 = new Link(
            0L,
            URLCreator.createURL("https://tinkoff.ru"),
            MIN,
            OffsetDateTime.now().minus(Duration.ofMinutes(60)),
            ""
        );
        jooqLinkRepository.add(link);
        jooqLinkRepository.add(link2);
        List<Link> linksFromDatabase = jooqLinkRepository.findLinksCheckedAfter(Duration.ofMinutes(10), 2);
        Assertions.assertThat(linksFromDatabase).map(Link::url).contains(link2.url());
    }
}
