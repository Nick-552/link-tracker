package edu.java.scrapper.domain.jdbc;

import edu.java.scrapper.model.ChatLink;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ChatLinkRepository {

    private final JdbcClient jdbcClient;

    @Transactional
    public void add(Long chatId, Long linkId) {
        jdbcClient
            .sql("INSERT INTO chats_links (chat_id, link_id) VALUES (?, ?)")
            .params(chatId, linkId)
            .update();
    }

    @Transactional
    public ChatLink remove(Long chatId, Long linkId) {
        return jdbcClient
            .sql("DELETE FROM chats_links WHERE chat_id = ? AND link_id = ? RETURNING *")
            .params(chatId, linkId)
            .query(ChatLink.class)
            .single();
    }

    @Transactional
    public void removeAllByChatId(Long chatId) {
        jdbcClient
            .sql("DELETE FROM chats_links WHERE chat_id = ?")
            .params(chatId)
            .update();
    }

    @Transactional
    public List<ChatLink> findAllByChatId(Long chatId) {
        return jdbcClient.sql("SELECT * FROM chats_links WHERE chat_id = ?")
            .params(chatId)
            .query(ChatLink.class)
            .list();
    }

    @Transactional
    public List<ChatLink> findAllByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT * FROM chats_links WHERE link_id = ?")
            .params(linkId)
            .query(ChatLink.class)
            .list();
    }
}
