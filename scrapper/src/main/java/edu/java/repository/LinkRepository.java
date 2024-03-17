package edu.java.repository;

import edu.java.dto.Link;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {
    List<Link> findAll();

    Long add(Link link);

    Long remove(long linkId);

    Link findById(long linkId);

    Link findByUrl(URL url);

    List<Link> findLinksCheckedAfter(Duration afterDuration, int limit);

    void update(long linkId, OffsetDateTime lastUpdate);
}
