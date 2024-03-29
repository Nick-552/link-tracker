package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.model.Chat;
import edu.java.scrapper.service.links.LinksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
public abstract class TgChatsServiceTest<T extends TgChatsService> extends IntegrationEnvironment {

    private T tgChatsService;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private LinksService linksService;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp() {
        tgChatsService = createInstance();
    }

    @Test
    @Transactional
    @Rollback
    public void registerChat() {
        var chat = new Chat(1L);
        tgChatsService.registerChat(new Chat(1L));
        flush();
        var optionalChatFromBd = jdbcClient
            .sql("SELECT * FROM chats WHERE id = 1")
            .query(Chat.class)
            .optional();
        assertThat(optionalChatFromBd).isPresent();
        assertThatExceptionOfType(ChatAlreadyRegisteredException.class)
            .isThrownBy(() -> tgChatsService.registerChat(chat));
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChat() {
        var chat = new Chat(1L);
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> tgChatsService.deleteChat(1L));
        tgChatsService.registerChat(chat);
        flush();
        linksService
            .addLinkToChat(
                chat.id(),
                new AddLinkRequest(
                    URI.create("https://github.com/Nick-552/link-tracker")
                )
            );
        flush();
        tgChatsService.deleteChat(1L);
        flush();
        var optionalChatFromBd = jdbcClient
            .sql("SELECT * FROM chats WHERE id = 1")
            .query(Chat.class)
            .optional();
        assertThat(optionalChatFromBd).isEmpty();
    }

    // optional (now only for jpa)
    public void flush() {
    }
}
