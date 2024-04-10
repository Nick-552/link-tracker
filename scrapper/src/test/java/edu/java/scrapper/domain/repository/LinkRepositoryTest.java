package edu.java.scrapper.domain.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@Log4j2
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
public abstract class LinkRepositoryTest<T extends LinkRepository> extends IntegrationEnvironment {

    private T linkRepository;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp() {
        linkRepository = createInstance();
    }

    @Test
    @Transactional
    @Rollback
    void addAndFind_basicTest() {
        var linkAdded = linkRepository.add(
            URI.create("https://github.com/nick-552/link-tracker"),
            OffsetDateTime.now()
        );
        var linkGotByUrl = linkRepository.findByUrl(linkAdded.url()).orElseThrow();
        var linkGotById = linkRepository.findById(linkAdded.id()).orElseThrow();
        assertThat(linkGotByUrl).isEqualTo(linkGotById);
        assertThat(linkGotById).isEqualTo(linkAdded);
        // duplicate link not throws exception
        linkRepository.add(linkAdded.url(), linkAdded.lastCheckAt());
    }

    @Test
    @Transactional
    @Rollback
    void findByUrl_whenNotExists_shouldReturnOptionalEmpty() {
        var linkGotByUrl = linkRepository.findByUrl(URI.create("https://no.such.url/in/database"));
        assertThat(linkGotByUrl).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findById_whenNotExists_shouldThrowException() {
        var linkGotById = linkRepository.findById(-1L);
        assertThat(linkGotById).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void removeById_basicTest() {
        var linkAdded = linkRepository.add(
            URI.create("https://github.com/nick-552/link-tracker"),
            OffsetDateTime.now()
        );
        linkRepository.removeById(linkAdded.id());
        assertThat(linkRepository.findById(linkAdded.id())).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void removeById_whenNotExists_shouldNotThrowException() {
        // test not throws exception
        linkRepository.removeById(-1L);
    }

    @Test
    @Transactional
    @Rollback
    void update_basicTest() {
        var linkAdded = linkRepository.add(
            URI.create("https://github.com/nick-552/link-tracker"),
            OffsetDateTime.now()
        );
        var updated = linkAdded.getUpdated(OffsetDateTime.now().plusHours(5));
        linkRepository.update(updated);
        var linkGotById = linkRepository.findById(linkAdded.id()).orElseThrow();
        assertThat(linkGotById.lastCheckAt().truncatedTo(ChronoUnit.MILLIS))
            .isAtSameInstantAs(updated.lastCheckAt().truncatedTo(ChronoUnit.MILLIS));
        assertThat(linkGotById.lastUpdatedAt().truncatedTo(ChronoUnit.MILLIS))
            .isAtSameInstantAs(updated.lastUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    @Transactional
    @Rollback
    void findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime() {
        var link1 = linkRepository.add(URI.create("link1"), OffsetDateTime.now());
        var link2 = linkRepository.add(URI.create("link2"), OffsetDateTime.now());
        var link3 = linkRepository.add(URI.create("link3"), OffsetDateTime.now());
        var link4 = linkRepository.add(URI.create("link4"), OffsetDateTime.now());
        var links = List.of(link1, link2, link3, link4);
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
        link1 = linkRepository.findById(link1.id()).orElseThrow();
        link2 = linkRepository.findById(link2.id()).orElseThrow();
        link3 = linkRepository.findById(link3.id()).orElseThrow();
        link4 = linkRepository.findById(link4.id()).orElseThrow();
        var actual = linkRepository.findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
            4,
            Duration.ofMinutes(150)
        );
        assertThat(actual).containsExactly(link3, link2);
        actual = linkRepository.findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
            10,
            Duration.ofMinutes(0)
        );
        assertThat(actual).containsExactly(link3, link2, link1, link4);
        actual = linkRepository.findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
            3,
            Duration.ofMinutes(0)
        );
        assertThat(actual).containsExactly(link3, link2, link1);
    }
}
