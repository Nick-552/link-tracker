package edu.java.scrapper.domain.repository;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    List<Link> findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
        int limit, Duration minTimeSinceLastUpdate
    );

    Link findById(Long id);

    Optional<Link> findByUrl(URI url);

    Link add(URI url, OffsetDateTime updatedAt);

    void update(Link link);

    void remove(Long id);
}
