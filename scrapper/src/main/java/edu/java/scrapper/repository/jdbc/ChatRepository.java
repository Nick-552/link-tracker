package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.model.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ChatRepository {

    private final JdbcClient jdbcClient;

    public void add(Chat chat) {
        jdbcClient
            .sql("INSERT INTO chats (id) VALUES (?)")
            .params(chat.id())
            .update();
    }

    public void removeById(Long id) {
        jdbcClient
            .sql("DELETE FROM chats WHERE id = ?")
            .params(id)
            .update();
    }

    public List<Chat> findAll() {
        return jdbcClient
            .sql("SELECT * FROM chats")
            .query(Chat.class)
            .list();
    }

    public Chat findById(Long id) {
        return jdbcClient
            .sql("SELECT * FROM chats WHERE id = ?")
            .params(id)
            .query(Chat.class)
            .single();
    }
}
