package edu.java.scrapper.domain.jooq.repository;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.model.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.tables.Links.LINKS;

@RequiredArgsConstructor
public class LinkRepositoryJooqImpl implements LinkRepository {

    private final DSLContext dslContext;

    @Override
    public List<Link> findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
        int limit,
        Duration minTimeSinceLastUpdate
    ) {
        return dslContext.selectFrom(LINKS)
            .where(LINKS.LAST_CHECK_AT.lt(OffsetDateTime.now().minus(minTimeSinceLastUpdate)))
            .orderBy(LINKS.LAST_CHECK_AT.asc())
            .limit(limit)
            .fetchInto(Link.class);
    }

    @Override
    public Link findById(Long id) {
        return dslContext.selectFrom(LINKS)
            .where(LINKS.ID.eq(id))
            .fetchOneInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        return dslContext.selectFrom(LINKS)
            .where(LINKS.URL.eq(String.valueOf(url)))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Link add(URI url, OffsetDateTime updatedAt) {
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT, LINKS.LAST_CHECK_AT)
            .values(String.valueOf(url), updatedAt, OffsetDateTime.now())
            .onConflictDoNothing()
            .execute();
        return findByUrl(url).get();
    }

    @Override
    public void update(Link link) {
        dslContext.update(LINKS)
            .set(LINKS.LAST_CHECK_AT, link.lastCheckAt())
            .set(LINKS.LAST_UPDATED_AT, link.lastUpdatedAt())
            .where(LINKS.ID.eq(link.id()))
            .execute();
    }

    @Override
    public void remove(Long id) {
        dslContext.delete(LINKS)
            .where(LINKS.ID.eq(id))
            .execute();
    }
}
