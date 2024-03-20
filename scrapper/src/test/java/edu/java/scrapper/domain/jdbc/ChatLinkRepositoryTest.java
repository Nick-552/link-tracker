package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.ChatLink;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Log4j2
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
class ChatLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private ChatLinkRepository chatLinkRepository;

    @Autowired
    private JdbcClient jdbcClient;

    List<Long> setUpChatsAndLinks() {
        setUpChats();
        return setUpLinks();
    }

    void setUpChats() {
        jdbcClient.sql("INSERT INTO chats VALUES (?)").params(1L).update();
        jdbcClient.sql("INSERT INTO chats VALUES (?)").params(2L).update();
    }

    List<Long> setUpLinks() {
        return List.of(
            jdbcClient.sql("INSERT INTO links VALUES (DEFAULT, ?, ?, ?) RETURNING id")
                .params("https://google.com", OffsetDateTime.now(), OffsetDateTime.now())
                .query(Long.class).single(),
            jdbcClient.sql("INSERT INTO links VALUES (DEFAULT, ?, ?, ?) RETURNING id")
                .params("https://yandex.ru", OffsetDateTime.now(), OffsetDateTime.now())
                .query(Long.class).single()
        );
    }

    @Test
    @Transactional
    @Rollback
    void add_whenLinkNotExistOrChatNotExist_shouldThrowDataIntegrityViolationException() {
        assertThatExceptionOfType(DataIntegrityViolationException.class)
            .isThrownBy(() ->chatLinkRepository.add(1L, 1L));
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        setUpChats();
        var keys = setUpLinks();
        chatLinkRepository.add(1L, keys.get(0));
        chatLinkRepository.add(1L, keys.get(1));
        chatLinkRepository.add(2L, keys.get(0));
        var actual = jdbcClient.sql("SELECT * FROM chats_links").query(ChatLink.class).list();
        assertThat(actual).hasSize(3);
        assertThatExceptionOfType(DataIntegrityViolationException.class)
            .isThrownBy(() -> chatLinkRepository.add(1L, keys.get(0)));
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        var keys = setUpChatsAndLinks();
        chatLinkRepository.add(1L, keys.get(0));
        chatLinkRepository.add(1L, keys.get(1));
        chatLinkRepository.add(2L, keys.get(0));
        chatLinkRepository.add(2L, keys.get(1));
        var removed =chatLinkRepository.remove(1L, keys.get(0));
        assertThat(removed.chatId()).isEqualTo(1L);
        assertThat(removed.linkId()).isEqualTo(keys.get(0));
        var remaining = jdbcClient.sql("SELECT * FROM chats_links").query(ChatLink.class).list();
        assertThat(remaining).hasSize(3);
    }

    @Test
    @Transactional
    @Rollback
    void removeAllByChatId() {
        var keys = setUpChatsAndLinks();
        chatLinkRepository.add(1L, keys.get(0));
        chatLinkRepository.add(1L, keys.get(1));
        chatLinkRepository.add(2L, keys.get(0));
        chatLinkRepository.removeAllByChatId(1L);
        var remaining = jdbcClient.sql("SELECT * FROM chats_links").query(ChatLink.class).list();
        assertThat(remaining).hasSize(1);
        assertThat(remaining.getFirst().chatId()).isEqualTo(2L);
        assertThat(remaining.getFirst().linkId()).isEqualTo(keys.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void findAllByChatId() {
        var keys = setUpChatsAndLinks();
        chatLinkRepository.add(1L, keys.get(0));
        chatLinkRepository.add(1L, keys.get(1));
        chatLinkRepository.add(2L, keys.get(0));
        var links = chatLinkRepository.findAllByChatId(2L);
        assertThat(links).hasSize(1);
        assertThat(links.getFirst().chatId()).isEqualTo(2L);
        assertThat(links.getFirst().linkId()).isEqualTo(keys.getFirst());
    }

    @Test
    @Transactional
    @Rollback
    void findAllByLinkId() {
        var keys = setUpChatsAndLinks();
        chatLinkRepository.add(1L, keys.get(0));
        chatLinkRepository.add(1L, keys.get(1));
        chatLinkRepository.add(2L, keys.get(0));
        var links = chatLinkRepository.findAllByLinkId(keys.get(1));
        assertThat(links).hasSize(1);
        assertThat(links.getFirst().chatId()).isEqualTo(1L);
        assertThat(links.getFirst().linkId()).isEqualTo(keys.get(1));
    }
}
