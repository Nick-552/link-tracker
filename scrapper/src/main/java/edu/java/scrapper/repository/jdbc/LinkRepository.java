package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.model.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LinkRepository {

    private final JdbcClient jdbcClient;

    private static final String SELECT_BY_URL = "SELECT * FROM links WHERE url = ?";

    @Transactional
    public List<Link> findAllOrderedByLastCheckTime(int limit, Duration minTimeSinceLastUpdate) {
        return jdbcClient.sql("""
            SELECT *
            FROM links
            WHERE last_check_at < ?
            ORDER BY last_check_at, last_updated_at DESC
            LIMIT ?
            """)
            .params(OffsetDateTime.now().minus(minTimeSinceLastUpdate), limit)
            .query(Link.class)
            .list();
    }

    @Transactional
    public Link findById(Long id) {
        return jdbcClient
            .sql("SELECT * FROM links WHERE id = ?")
            .params(id)
            .query(Link.class)
            .single();
    }

    @Transactional
    public Link findByUrl(URI url) {
        return jdbcClient
            .sql(SELECT_BY_URL)
            .params(url.toString())
            .query(Link.class)
            .single();
    }

    @Transactional
    public Link add(URI url, OffsetDateTime updatedAt) {
        if (jdbcClient.sql(SELECT_BY_URL).params(url.toString()).query(Link.class).list().isEmpty()) {
            jdbcClient
                .sql("INSERT INTO links (url, last_updated_at, last_check_at) VALUES (?, ?, ?)")
                .params(url.toString(), updatedAt, OffsetDateTime.now())
                .update();
        }
        return findByUrl(url);
    }

    @Transactional
    public void update(Link link) {
        jdbcClient
            .sql("UPDATE links SET last_updated_at = :updatedAt, last_check_at = :checkedAt WHERE id = :id")
            .param("id", link.id())
            .param("updatedAt", link.lastUpdatedAt())
            .param("checkedAt", link.lastCheckAt())
            .update();
    }

    @Transactional
    public void remove(Long id) {
        jdbcClient
            .sql("DELETE FROM links WHERE id = :id")
            .param("id", id)
            .update();
    }
}