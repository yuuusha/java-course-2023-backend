package edu.java.repository.jooq;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.LINK;

@Repository
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext dslContext;

    public JooqLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Link> findAll() {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .fetchInto(Link.class);
    }

    @Override
    public Long add(Link link) {
        return dslContext.insertInto(
                LINK,
                LINK.URL,
                LINK.LAST_UPDATE,
                LINK.LAST_CHECK,
                LINK.META_INFO
            ).values(
                String.valueOf(link.url()),
                link.lastUpdate(),
                link.lastCheck(),
                link.metaInfo()
            ).returning(LINK.LINK_ID)
            .fetchOne(LINK.LINK_ID);
    }

    @Override
    public Long remove(long linkId) {
        return dslContext.delete(LINK)
            .where(LINK.LINK_ID.eq(linkId))
            .returning(LINK.LINK_ID)
            .fetchOne(LINK.LINK_ID);
    }

    @Override
    public Optional<Link> findById(long linkId) {
        return Optional.ofNullable(
            dslContext.select(LINK.fields())
                .from(LINK)
                .where(LINK.LINK_ID.eq(linkId))
                .fetchOneInto(Link.class)
        );
    }

    @Override
    public Optional<Link> findByUrl(URL url) {
        return Optional.ofNullable(
            dslContext.select(LINK.fields())
                .from(LINK)
                .where(LINK.URL.eq(String.valueOf(url)))
                .fetchOneInto(Link.class)
        );
    }

    @Override
    public List<Link> findLinksCheckedAfter(Duration after, int limit) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.LAST_CHECK.lt(OffsetDateTime.now().minus(after)))
            .limit(limit)
            .fetchInto(Link.class);
    }


    @Override
    public void update(long linkId, OffsetDateTime lastUpdate, String metaInfo) {
        dslContext.update(LINK)
            .set(LINK.LAST_UPDATE, lastUpdate)
            .set(LINK.LAST_CHECK, OffsetDateTime.now())
            .set(LINK.META_INFO, metaInfo)
            .where(LINK.LINK_ID.eq(linkId))
            .execute();
    }

    @Override
    public void checkNow(long id) {
        dslContext.update(LINK)
            .set(LINK.LAST_CHECK, OffsetDateTime.now())
            .where(LINK.LINK_ID.eq(id))
            .execute();
    }
}
