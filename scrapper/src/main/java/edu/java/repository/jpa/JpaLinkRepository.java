package edu.java.repository.jpa;

import edu.java.domain.jpa.entity.LinkEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    Optional<LinkEntity> findByUrl(String url);

    List<LinkEntity> findAllByLastCheckBefore(OffsetDateTime afterDuration, Limit limit);
}
