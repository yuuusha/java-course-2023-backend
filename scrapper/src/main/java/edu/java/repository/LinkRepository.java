package edu.java.repository;

import edu.java.dto.Link;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAll();

    Long add(Link link);

    Long remove(long linkId);

    Optional<Link> findById(long linkId);

    Optional<Link> findByUrl(URL url);

    List<Link> findLinksCheckedAfter(Duration afterDuration, int limit);

    void update(long linkId, OffsetDateTime lastUpdate, String metaInfo);

    void checkNow(long id);
}
