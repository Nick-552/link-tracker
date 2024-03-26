package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.domain.repository.jdbc.ChatRepositoryJdbcImpl;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.model.Chat;
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
class ChatRepositoryJdbcImplTest extends IntegrationEnvironment {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ChatRepositoryJdbcImpl chatRepositoryJdbcImpl;

    List<Chat> addChats() {
        var chats = List.of(
            new Chat(1L),
            new Chat(2L),
            new Chat(3L)
        );
        chats.forEach(chat -> jdbcClient
            .sql("INSERT INTO chats (id) VALUES (?)")
            .params(chat.id())
            .update());
        return chats;
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        var chats = jdbcClient.sql("SELECT * FROM chats").query(Chat.class).list();
        assertThat(chats).isEmpty();
        var chat = new Chat(1L);
        chatRepositoryJdbcImpl.add(chat);
        chats = jdbcClient.sql("SELECT * FROM chats").query(Chat.class).list();
        assertThat(chats).containsExactly(chat);
        var chat2 = new Chat(1L);
        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() -> chatRepositoryJdbcImpl.add(chat2));
    }

    @Test
    @Transactional
    @Rollback
    void removeById() {
        var chats = addChats();
        var chats1 = jdbcClient.sql("SELECT * FROM chats").query(Chat.class).list();
        assertThat(chats1).containsExactlyElementsOf(chats);
        chatRepositoryJdbcImpl.removeById(1L);
        var chats2 = jdbcClient.sql("SELECT * FROM chats").query(Chat.class).list();
        assertThat(chats2).containsExactlyElementsOf(chats.subList(1, chats.size()));
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        var expected = addChats();
        var actual = chatRepositoryJdbcImpl.findAll();
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    @Transactional
    @Rollback
    void findById() {
        addChats();
        var chat = chatRepositoryJdbcImpl.findById(1L);
        assertThat(chat).isNotNull();
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> chatRepositoryJdbcImpl.findById(4L));
    }
}
