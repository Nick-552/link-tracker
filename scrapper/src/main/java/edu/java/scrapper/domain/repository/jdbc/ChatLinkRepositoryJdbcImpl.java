package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.model.ChatLink;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatLinkRepositoryJdbcImpl implements ChatLinkRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void add(Long chatId, Long linkId) {
        jdbcClient
            .sql("INSERT INTO chats_links (chat_id, link_id) VALUES (?, ?)")
            .params(chatId, linkId)
            .update();
    }

    @Override
    public ChatLink remove(Long chatId, Long linkId) {
        return jdbcClient
            .sql("DELETE FROM chats_links WHERE chat_id = ? AND link_id = ? RETURNING *")
            .params(chatId, linkId)
            .query(ChatLink.class)
            .single();
    }

    @Override
    public void removeAllByChatId(Long chatId) {
        jdbcClient
            .sql("DELETE FROM chats_links WHERE chat_id = ?")
            .params(chatId)
            .update();
    }

    @Override
    public List<ChatLink> findAllByChatId(Long chatId) {
        return jdbcClient.sql("SELECT * FROM chats_links WHERE chat_id = ?")
            .params(chatId)
            .query(ChatLink.class)
            .list();
    }

    @Override
    public List<ChatLink> findAllByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT * FROM chats_links WHERE link_id = ?")
            .params(linkId)
            .query(ChatLink.class)
            .list();
    }

    @Override
    public boolean existsByChatIdAndLinkId(Long chatId, Long linkId) {
        return jdbcClient
            .sql("SELECT EXISTS(SELECT 1 FROM chats_links WHERE chat_id = ? AND link_id = ?)")
            .params(chatId, linkId)
            .query(Boolean.class)
            .single();
    }
}
