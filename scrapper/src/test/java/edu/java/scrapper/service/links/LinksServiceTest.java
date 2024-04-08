package edu.java.scrapper.service.links;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.model.ChatLink;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.update.UpdateInfoService;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@DirtiesContext
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
public abstract class LinksServiceTest<T extends LinksService> extends IntegrationEnvironment {

    private static final URI validUrl = URI.create("https://github.com/Nick-552/link-tracker");

    private T linksService;

    @Autowired
    private JdbcClient jdbcClient;

    @MockBean
    UpdateInfoServiceProvider updateInfoServiceProvider;

    @Mock
    private UpdateInfoService updateInfoService = Mockito.mock(UpdateInfoService.class);

    protected abstract T createInstance() ;

    @BeforeEach
    public void setUp() {
        linksService = createInstance();
        Mockito.when(updateInfoService.getLastUpdate(any()))
                .thenReturn(OffsetDateTime.now());
        Mockito.when(updateInfoServiceProvider.provide(any()))
            .thenReturn(updateInfoService);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkToChat() {
        var chatId = 1L;
        assertThatExceptionOfType(ChatNotRegisteredException.class)
            .isThrownBy(() -> linksService.addLinkToChat(chatId, new AddLinkRequest(validUrl)));
        jdbcClient.sql("INSERT INTO chats values (?)")
            .params(chatId)
            .update();
        var response = linksService.addLinkToChat(chatId, new AddLinkRequest(validUrl));
        flush();
        assertThat(response.url()).isEqualTo(validUrl);
        var optionalChatLinkFromDb = jdbcClient
            .sql("SELECT * FROM chats_links WHERE chat_id = ? AND link_id = ?")
            .params(chatId, response.id())
            .query(ChatLink.class)
            .optional();
        assertThat(optionalChatLinkFromDb).isPresent();
        assertThatExceptionOfType(LinkAlreadyTrackedException.class)
            .isThrownBy(() -> linksService.addLinkToChat(1L, new AddLinkRequest(validUrl)));
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkFromChat() {
        var chatId = 1L;
        jdbcClient.sql("INSERT INTO chats values (?)")
            .params(chatId)
            .update();
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> linksService.removeLinkFromChat(chatId, new RemoveLinkRequest(URI.create("link"))));
        var response = linksService.addLinkToChat(chatId, new AddLinkRequest(validUrl));
        flush();
        linksService.removeLinkFromChat(chatId, new RemoveLinkRequest(validUrl));
        flush();
        var optionalChatLinkFromDb = jdbcClient
            .sql("SELECT * FROM chats_links WHERE chat_id = ? AND link_id = ?")
            .params(chatId, response.id())
            .query(ChatLink.class)
            .optional();
        assertThat(optionalChatLinkFromDb).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void getLinksForChatId() {
        var chatId = 1L;
        jdbcClient.sql("INSERT INTO chats (id) values (?)")
            .params(chatId)
            .update();
        flush();
        var linksListResponse = linksService.getLinksForChatId(chatId);
        assertThat(linksListResponse.links()).isEmpty();
        var response1 = linksService.addLinkToChat(chatId, new AddLinkRequest(validUrl));
        flush();
        var response2 = linksService.addLinkToChat(chatId, new AddLinkRequest(URI.create("https://github.com/gavrilovds/link-tracker")));
        flush();
        linksListResponse = linksService.getLinksForChatId(chatId);
        assertThat(linksListResponse.links()).containsExactlyInAnyOrder(response1, response2);
    }

    @Test
    @Transactional
    @Rollback
    void getChatIdsForLink() {
        var chatId1 = 1L;
        var chatId2 = 2L;
        jdbcClient.sql("INSERT INTO chats (id) values (?)")
            .params(chatId1)
            .update();
        jdbcClient.sql("INSERT INTO chats (id) values (?)")
            .params(chatId2)
            .update();
        var chatId3 = 3L;
        jdbcClient.sql("INSERT INTO chats (id) values (?)")
            .params(chatId3)
            .update();
        var response1 = linksService.addLinkToChat(chatId1, new AddLinkRequest(validUrl));
        var response2 = linksService.addLinkToChat(chatId2, new AddLinkRequest(validUrl));
        flush();
        var chatIds = linksService.getChatIdsForLink(new Link(response1.id(), response1.url(), OffsetDateTime.now(), OffsetDateTime.now()));
        assertThat(chatIds).containsExactlyInAnyOrder(chatId1, chatId2);
    }

    @Test
    @Transactional
    @Rollback
    void updateLink() {
        jdbcClient.sql("INSERT INTO links (url, last_updated_at, last_check_at) VALUES (?, ?, ?)")
            .params(validUrl.toString(), OffsetDateTime.now().minusHours(4), OffsetDateTime.now().minusHours(5))
            .update();
        var link = jdbcClient.sql("SELECT * FROM links WHERE url = ?")
            .param(validUrl.toString())
            .query(Link.class)
            .single();
        var updatedLink = link.getUpdated(OffsetDateTime.now());
        linksService.updateLink(updatedLink);
        flush();
        var updatedLinkFromDb = jdbcClient.sql("SELECT * FROM links WHERE id = ?")
            .params(link.id())
            .query(Link.class)
            .single();
        assertThat(updatedLinkFromDb.lastUpdatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isAtSameInstantAs(updatedLink.lastUpdatedAt().truncatedTo(ChronoUnit.SECONDS));
        assertThat(updatedLinkFromDb.lastCheckAt().truncatedTo(ChronoUnit.SECONDS))
            .isAtSameInstantAs(updatedLink.lastCheckAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @Transactional
    @Rollback
    void listStaleLinks() {
        jdbcClient.sql("INSERT INTO links (url, last_updated_at, last_check_at) VALUES (?, ?, ?)")
            .params("link1", OffsetDateTime.now().minusHours(4), OffsetDateTime.now().minusHours(2))
            .update();
        jdbcClient.sql("INSERT INTO links (url, last_updated_at, last_check_at) VALUES (?, ?, ?)")
            .params("link2", OffsetDateTime.now().minusHours(4), OffsetDateTime.now().minusHours(1))
            .update();
        jdbcClient.sql("INSERT INTO links (url, last_updated_at, last_check_at) VALUES (?, ?, ?)")
            .params("link3", OffsetDateTime.now().minusHours(4), OffsetDateTime.now().minusHours(3))
            .update();
        jdbcClient.sql("INSERT INTO links (url, last_updated_at, last_check_at) VALUES (?, ?, ?)")
            .params("link4", OffsetDateTime.now().minusHours(4), OffsetDateTime.now())
            .update();
        var link1 = jdbcClient.sql("SELECT * FROM links WHERE url = ?")
            .params("link1")
            .query(Link.class)
            .single();
        var link2 = jdbcClient.sql("SELECT * FROM links WHERE url = ?")
            .params("link2")
            .query(Link.class)
            .single();
        var link3 = jdbcClient.sql("SELECT * FROM links WHERE url = ?")
            .params("link3")
            .query(Link.class)
            .single();
        var link4 = jdbcClient.sql("SELECT * FROM links WHERE url = ?")
            .params("link4")
            .query(Link.class)
            .single();
        var staleLinks = linksService.listStaleLinks(1, Duration.ofMinutes(90));
        assertThat(staleLinks).containsExactly(link3);
        staleLinks = linksService.listStaleLinks(3, Duration.ofMinutes(90));
        assertThat(staleLinks).containsExactly(link3, link1);
    }

    void flush() {
        // optional
    }
}
