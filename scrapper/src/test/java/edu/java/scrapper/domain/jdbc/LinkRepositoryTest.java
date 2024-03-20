package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
class LinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private LinkRepository linkRepository;

    List<Link> setUpLinks() {
        var links = List.of(
            linkRepository.add(URI.create("link1"), OffsetDateTime.now()),
            linkRepository.add(URI.create("link2"), OffsetDateTime.now()),
            linkRepository.add(URI.create("link3"), OffsetDateTime.now()),
            linkRepository.add(URI.create("link4"), OffsetDateTime.now())
        );
        for (int i = 0; i < 4; i++) {
            linkRepository.update(
                new Link(
                    links.get(i).id(),
                    links.get(i).url(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now().minusHours((i + 2) % 5) // 0->2, 1->3, 2->4, 3->0
                )
            );
        }
        return jdbcClient.sql("SELECT * FROM links").query(Link.class).list();
    }

    @Test
    @Transactional
    @Rollback
    void findAllOrderedByLastCheckTime() {
        var links = setUpLinks();
        var actual1 = linkRepository
            .findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
                4, Duration.ofMinutes(60)
            );
        var expected1 = List.of(
            links.get(2),
            links.get(1),
            links.get(0)
        );
        assertThat(actual1).containsExactlyElementsOf(expected1);
        var actual2 = linkRepository
            .findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
                2, Duration.ofMinutes(0)
            );
        var expected2 = List.of(
            links.get(2),
            links.get(1)
        );
        assertThat(actual2).containsExactlyElementsOf(expected2);
    }

    @Test
    @Transactional
    @Rollback
    void findById() {
        var links = setUpLinks();
        var link = linkRepository.findById(links.get(0).id());
        assertThat(link).isEqualTo(links.get(0));
    }

    @Test
    @Transactional
    @Rollback
    void findByUrl() {
        var links = setUpLinks();
        var link = linkRepository.findByUrl(links.get(0).url());
        assertThat(link).isEqualTo(links.get(0));
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        var linksUntilAdd = jdbcClient.sql("SELECT * FROM links").query(Link.class).list();
        assertThat(linksUntilAdd.size()).isEqualTo(0);
        linkRepository.add(URI.create("link1"), OffsetDateTime.now());
        var links = jdbcClient.sql("SELECT * FROM links").query(Link.class).list();
        assertThat(links.size()).isEqualTo(1);
        linkRepository.add(URI.create("link1"), OffsetDateTime.now().minusHours(1)); // should be ignored because url is the same
        links = jdbcClient.sql("SELECT * FROM links").query(Link.class).list();
        assertThat(links.size()).isEqualTo(1);

    }

    @Test
    @Transactional
    @Rollback
    void update() {
        var link = linkRepository.add(URI.create("link5"), OffsetDateTime.now());
        var updatedLink = new Link(
            link.id(), link.url(),
            OffsetDateTime.now().plusHours(1),
            OffsetDateTime.now().plusHours(1));
        linkRepository.update(updatedLink);
        assertThat(linkRepository.findById(link.id()).lastCheckAt())
            .isEqualToIgnoringNanos(updatedLink.lastCheckAt().withOffsetSameInstant(ZoneOffset.UTC));
        assertThat(linkRepository.findById(link.id()).lastUpdatedAt())
            .isEqualToIgnoringNanos(updatedLink.lastUpdatedAt().withOffsetSameInstant(ZoneOffset.UTC));
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        var links = setUpLinks();
        var links1 = jdbcClient.sql("SELECT * FROM links").query(Link.class).list();
        assertThat(links).containsExactlyElementsOf(links1);
        linkRepository.remove(links.get(0).id());
        var links2 = jdbcClient.sql("SELECT * FROM links").query(Link.class).list();
        assertThat(links2).containsExactlyElementsOf(links.subList(1, 4));
    }
}
