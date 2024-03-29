package edu.java.scrapper.service.links;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.model.ChatLink;
import jakarta.transaction.Transactional;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
public abstract class LinksServiceTest<T extends LinksService> extends IntegrationEnvironment {

    private T linksService;

    @Autowired
    private JdbcClient jdbcClient;

    protected abstract T createInstance() ;

    @BeforeEach
    public void setUp() {
        linksService = createInstance();
    }

    @Test
    @Transactional
    @Rollback
    void addLinkToChat() {
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> linksService.addLinkToChat(1L, new AddLinkRequest(URI.create("link"))));
        jdbcClient.sql("INSERT INTO chats values (?)")
            .params(1L)
            .update();
        var response = linksService.addLinkToChat(1L, new AddLinkRequest(URI.create("link")));
        flush();
        assertThat(response.url()).isEqualTo(URI.create("link"));
        var optionalChatLinkFromDb = jdbcClient
            .sql("SELECT * FROM chats_links WHERE chat_id = ? AND link_id = ?")
            .params(1L, response.id())
            .query(ChatLink.class)
            .optional();
        assertThat(optionalChatLinkFromDb).isPresent();
        assertThatExceptionOfType(LinkAlreadyTrackedException.class)
            .isThrownBy(() -> linksService.addLinkToChat(1L, new AddLinkRequest(URI.create("link"))));
    }

    void flush() {
        // optional
    }
}
