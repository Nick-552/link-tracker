package edu.java.scrapper.domain;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public interface LinkRepository {

    @Transactional
    List<Link> findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
        int limit, Duration minTimeSinceLastUpdate
    );

    @Transactional
    Link findById(Long id);

    @Transactional
    Optional<Link> findByUrl(URI url);

    @Transactional
    Link add(URI url, OffsetDateTime updatedAt);

    @Transactional
    void update(Link link);

    @Transactional
    void remove(Long id);
}
