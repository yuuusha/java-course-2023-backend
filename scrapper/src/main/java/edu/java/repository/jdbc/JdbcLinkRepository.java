package edu.java.repository.jdbc;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private static final String URL = "url";
    private static final String LAST_UPDATE = "last_update";
    private static final String LAST_CHECK = "last_check";
    private static final String LINK_ID = "link_id";

    private final JdbcClient client;

    @Override
    public List<Link> findAll() {
        return client.sql("SELECT * FROM link")
            .query(Link.class)
            .list();
    }

    @Override
    public Long add(Link link) {
        return client.sql("""
                INSERT INTO
                  link(url, last_update, last_check)
                VALUES
                  (:url, :last_update, :last_check)
                ON CONFLICT (url)
                DO UPDATE SET last_update = :last_update, last_check = :last_check
                RETURNING link_id""")
            .param(URL, String.valueOf(link.url()))
            .param(LAST_UPDATE, link.lastUpdate())
            .param(LAST_CHECK, link.lastCheck())
            .query(Long.class)
            .single();
    }

    @Override
    public Long remove(long linkId) {
        return client.sql("DELETE FROM link WHERE link_id = :link_id RETURNING link_id")
            .param(LINK_ID, linkId)
            .query(Long.class)
            .single();
    }

    @Override
    public Link findById(long linkId) {
        return client.sql("SELECT * FROM link WHERE link_id = :link_id")
            .param(LINK_ID, linkId)
            .query(Link.class)
            .single();
    }

    @Override
    public Link findByUrl(URL url) {
        String urlString = String.valueOf(url);
        return client.sql("SELECT * FROM link WHERE url = :url")
            .param(URL, urlString)
            .query(Link.class).optional().orElse(null);
    }

    @Override
    public List<Link> findLinksCheckedAfter(Duration afterDuration, int limit) {
        return client.sql("""
                SELECT
                  *
                FROM
                  link
                WHERE
                  link.last_check < :last_check
                ORDER BY last_check
                LIMIT :limit
                """
            )
            .param(LAST_CHECK, OffsetDateTime.now().minus(afterDuration))
            .param("limit", limit)
            .query(Link.class)
            .list();
    }

    @Override
    public void update(long linkId, OffsetDateTime lastUpdate) {
        client.sql(
                """
                    UPDATE link
                    SET last_check = :last_check, last_update = :last_update
                    WHERE link_id = :link_id
                    """)
            .param(LAST_CHECK, OffsetDateTime.now())
            .param(LAST_UPDATE, lastUpdate)
            .param(LINK_ID, linkId)
            .update();
    }
}
