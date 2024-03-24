package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.model.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryJdbcImpl implements ChatRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void add(Chat chat) {
        jdbcClient
            .sql("INSERT INTO chats (id) VALUES (?)")
            .params(chat.id())
            .update();
    }

    @Override
    public void removeById(Long id) {
        jdbcClient
            .sql("DELETE FROM chats WHERE id = ?")
            .params(id)
            .update();
    }

    @Override
    public List<Chat> findAll() {
        return jdbcClient
            .sql("SELECT * FROM chats")
            .query(Chat.class)
            .list();
    }

    @Override
    public Chat findById(Long id) {
        return jdbcClient
            .sql("SELECT * FROM chats WHERE id = ?")
            .params(id)
            .query(Chat.class)
            .optional()
            .orElseThrow(ChatNotRegisteredException::new);
    }

    @Override
    public boolean existsById(Long id) {
        return jdbcClient.sql("SELECT EXISTS(SELECT id FROM chats WHERE id = ?)")
            .params(id)
            .query(Boolean.class)
            .single();
    }
}
