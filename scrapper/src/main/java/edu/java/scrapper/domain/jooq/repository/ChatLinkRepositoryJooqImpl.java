package edu.java.scrapper.domain.jooq.repository;

import edu.java.scrapper.domain.ChatLinkRepository;
import edu.java.scrapper.model.ChatLink;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.tables.ChatsLinks.CHATS_LINKS;

@RequiredArgsConstructor
public class ChatLinkRepositoryJooqImpl implements ChatLinkRepository {

    private final DSLContext dslContext;

    @Override
    public void add(Long chatId, Long linkId) {
        dslContext.insertInto(CHATS_LINKS, CHATS_LINKS.CHAT_ID, CHATS_LINKS.LINK_ID)
            .values(chatId, linkId)
            .execute();
    }

    @Override
    public ChatLink remove(Long chatId, Long linkId) {
        return dslContext.deleteFrom(CHATS_LINKS)
            .where(CHATS_LINKS.CHAT_ID.eq(chatId).and(CHATS_LINKS.LINK_ID.eq(linkId)))
            .returning()
            .fetchOneInto(ChatLink.class);
    }

    @Override
    public void removeAllByChatId(Long chatId) {
        dslContext.deleteFrom(CHATS_LINKS)
            .where(CHATS_LINKS.CHAT_ID.eq(chatId))
            .execute();
    }

    @Override
    public List<ChatLink> findAllByChatId(Long chatId) {
        return dslContext.selectFrom(CHATS_LINKS)
            .where(CHATS_LINKS.CHAT_ID.eq(chatId))
            .fetchInto(ChatLink.class);
    }

    @Override
    public List<ChatLink> findAllByLinkId(Long linkId) {
        return dslContext.selectFrom(CHATS_LINKS)
            .where(CHATS_LINKS.LINK_ID.eq(linkId))
            .fetchInto(ChatLink.class);
    }

    @Override
    public boolean existsByChatIdAndLinkId(Long chatId, Long linkId) {
        return dslContext.fetchExists(
            dslContext.selectFrom(CHATS_LINKS)
                .where(CHATS_LINKS.CHAT_ID.eq(chatId).and(CHATS_LINKS.LINK_ID.eq(linkId)))
        );
    }
}
