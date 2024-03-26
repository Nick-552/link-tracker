package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.model.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.tables.Chats.CHATS;

@RequiredArgsConstructor
public class ChatRepositoryJooqImpl implements ChatRepository {

    private final DSLContext dslContext;


    @Override
    public void add(Chat chat) {
        dslContext.insertInto(CHATS, CHATS.ID)
            .values(chat.id())
            .execute();
    }

    @Override
    public void removeById(Long id) {
        dslContext.deleteFrom(CHATS)
            .where(CHATS.ID.eq(id))
            .execute();
    }

    @Override
    public List<Chat> findAll() {
        return dslContext.selectFrom(CHATS)
            .fetchInto(Chat.class);
    }

    @Override
    public Chat findById(Long id) {
        return dslContext.selectFrom(CHATS)
            .where(CHATS.ID.eq(id))
            .fetchOneInto(Chat.class);
    }

    @Override
    public boolean existsById(Long id) {
        return dslContext.fetchExists(
            dslContext.selectFrom(CHATS)
                .where(CHATS.ID.eq(id))
        );
    }
}
