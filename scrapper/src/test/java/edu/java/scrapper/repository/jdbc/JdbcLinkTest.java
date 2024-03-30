package edu.java.scrapper.repository.jdbc;

import edu.java.dto.Link;
import edu.java.repository.jdbc.JdbcLinkRepository;
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
public class JdbcLinkTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    private Link link;
    private Link link2;

    @BeforeEach
    void setUp() {
        link = new Link(0L, URLCreator.createURL("https://google.com"), OffsetDateTime.MIN, OffsetDateTime.MAX, "");
        link2 = new Link(0L, URLCreator.createURL("https://tinkoff.ru"), OffsetDateTime.MIN, OffsetDateTime.MAX, "");
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        Long id = jdbcLinkRepository.add(link);
        Optional<Link> linkFromDatabase = jdbcLinkRepository.findById(id);
        Assertions.assertThat(linkFromDatabase.get().url()).isEqualTo(link.url());
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkTest() {
        jdbcLinkRepository.add(link);
        Optional<Link> linkFromDatabase = jdbcLinkRepository.findByUrl(link.url());
        jdbcLinkRepository.remove(linkFromDatabase.get().linkId());
        Optional<Link> actualResult = jdbcLinkRepository.findByUrl(link.url());
        Assertions.assertThat(actualResult).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinks() {
        jdbcLinkRepository.add(link);
        jdbcLinkRepository.add(link2);
        List<Link> linksFromDatabase = jdbcLinkRepository.findAll();
        Assertions.assertThat(linksFromDatabase).map(Link::url).contains(link.url(), link2.url());
    }

    @Test
    @Transactional
    @Rollback
    void oldLinksTest() {
        link = new Link(0L, URLCreator.createURL("https://google.com"), OffsetDateTime.MIN, OffsetDateTime.now(), "");
        link2 = new Link(
            0L,
            URLCreator.createURL("https://tinkoff.ru"),
            OffsetDateTime.MIN,
            OffsetDateTime.now().minus(Duration.ofMinutes(60)),
            ""
        );
        jdbcLinkRepository.add(link);
        jdbcLinkRepository.add(link2);
        List<Link> linksFromDatabase = jdbcLinkRepository.findLinksCheckedAfter(Duration.ofMinutes(10), 2);
        Assertions.assertThat(linksFromDatabase).map(Link::url).contains(link2.url());
    }
}
