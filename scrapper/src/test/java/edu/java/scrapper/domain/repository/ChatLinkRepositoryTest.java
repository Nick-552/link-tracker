package edu.java.scrapper.domain.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Chat;
import edu.java.scrapper.model.Link;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@Log4j2
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
public abstract class ChatLinkRepositoryTest<T extends ChatLinkRepository> extends IntegrationEnvironment {

    private T chatLinkRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private LinkRepository linkRepository;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp() {
        chatLinkRepository = createInstance();
    }

    public List<Chat> setUpChats(int size) {
        List<Chat> chats = new ArrayList<>(size);
        for (long i = 0; i < size; i++) {
            var chat = new Chat(i);
            chatRepository.add(chat);
            chats.add(chat);
        }
        return chats;

    }

    public List<Link> setUpLinks(int size) {
        List<Link> links = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            links.add(
                linkRepository.add(
                    URI.create("https://tinkoff.ru" + i),
                    OffsetDateTime.now()
                )
            );
        }
        return links;
    }

    public void setUpChatsLinks(List<Chat> chats, List<Link> links) {
        for (var chat : chats) {
            for (var link : links) {
                chatLinkRepository.add(chat.id(), link.id());
            }
        }
    }

    @Test
    @Transactional
    @Rollback
    public void addAndRemove_basicTest() {
        var chat = setUpChats(1).getFirst();
        var link = setUpLinks(1).getFirst();
        chatLinkRepository.add(chat.id(), link.id());
        var chatLinkOptional = chatLinkRepository.remove(chat.id(), link.id());
        assertThat(chatLinkOptional).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    public  void remove_whenNotExists_shouldReturnOptionalEmpty() {
        var chatLinkOptional = chatLinkRepository.remove(1L, 1L);
        assertThat(chatLinkOptional).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void removeByChatIdAndFindAllByChatId() {
        setUpChatsLinks(setUpChats(4), setUpLinks(3));
        chatLinkRepository.removeAllByChatId(1L);
        assertThat(chatLinkRepository.findAllByChatId(1L))
            .isEmpty();
        assertThat(chatLinkRepository.findAllByChatId(2L))
            .hasSize(3);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByLinkId() {
        var chats = setUpChats(2);
        var links = setUpLinks(3);
        setUpChatsLinks(chats, links);
        assertThat(chatLinkRepository.findAllByLinkId(links.getFirst().id()))
            .hasSize(2);
        assertThat(chatLinkRepository.findAllByLinkId(-1L))
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void existsByChatIdAndLinkId() {
        var chat = setUpChats(1).getFirst();
        var link = setUpLinks(1).getFirst();
        assertThat(chatLinkRepository.existsByChatIdAndLinkId(chat.id(), link.id()))
            .isFalse();
        chatLinkRepository.add(chat.id(), link.id());
        assertThat(chatLinkRepository.existsByChatIdAndLinkId(chat.id(), link.id()))
            .isTrue();
        assertThat(chatLinkRepository.existsByChatIdAndLinkId(chat.id(), -1L))
            .isFalse();
        assertThat(chatLinkRepository.existsByChatIdAndLinkId(-1L, link.id()))
            .isFalse();
    }
}
