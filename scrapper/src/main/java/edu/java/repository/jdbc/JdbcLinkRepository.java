package edu.java.repository.jdbc;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcClient client;

    private static final String URL = "url";

    private static final String LAST_UPDATE = "last_update";

    private static final String LAST_CHECK = "last_check";

    private static final String LINK_ID = "link_id";

    private static final String META_INFO = "meta_info";


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
                  link(url, last_update, last_check, meta_info)
                VALUES
                  (:url, :last_update, :last_check, :meta_info)
                ON CONFLICT (url)
                DO UPDATE SET last_update = :last_update, last_check = :last_check, meta_info = :meta_info
                RETURNING link_id""")
            .param(URL, String.valueOf(link.url()))
            .param(LAST_UPDATE, link.lastUpdate())
            .param(LAST_CHECK, link.lastCheck())
            .param(META_INFO, link.metaInfo())
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
    public Optional<Link> findById(long linkId) {
        return client.sql("SELECT * FROM link WHERE link_id = :link_id")
            .param(LINK_ID, linkId)
            .query(Link.class)
            .optional();
    }

    @Override
    public Optional<Link> findByUrl(URL url) {
        String urlString = String.valueOf(url);
        return client.sql("SELECT * FROM link WHERE url = :url")
            .param(URL, urlString)
            .query(Link.class).optional();
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
    public void update(long linkId, OffsetDateTime lastUpdate, String metaInfo) {
        client.sql(
                """
                    UPDATE link
                    SET last_check = :last_check, last_update = :last_update, meta_info = :meta_info
                    WHERE link_id = :link_id
                    """)
            .param(LAST_CHECK, OffsetDateTime.now())
            .param(LAST_UPDATE, lastUpdate)
            .param(LINK_ID, linkId)
            .param(META_INFO, metaInfo)
            .update();
    }

    @Override
    public void checkNow(long id) {
        client.sql("UPDATE link SET last_check = :last_check WHERE link_id = :link_id")
            .param(LAST_CHECK, OffsetDateTime.now())
            .param(LINK_ID, id)
            .update();
    }
}
