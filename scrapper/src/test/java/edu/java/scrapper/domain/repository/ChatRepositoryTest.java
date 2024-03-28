package edu.java.scrapper.domain.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.model.Chat;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Log4j2
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
public abstract class ChatRepositoryTest<T extends ChatRepository> extends IntegrationEnvironment {

    private T chatRepository;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp() {
        chatRepository = createInstance();
    }

    @Test
    @Transactional
    @Rollback
    public void addAndFindById() {
        Chat chat = new Chat(1L);
        chatRepository.add(chat);
        assertThat(chatRepository.findById(chat.id()).orElseThrow())
            .isEqualTo(chat);
        Chat chat1 = new Chat(1L);
        assertThatExceptionOfType(DuplicateKeyException.class)
            .isThrownBy(() -> chatRepository.add(chat1));
    }

    @Test
    @Transactional
    @Rollback
    public void findAll() {
        Chat chat1 = new Chat(1L);
        Chat chat2 = new Chat(2L);
        Chat chat3 = new Chat(3L);
        chatRepository.add(chat1);
        chatRepository.add(chat2);
        chatRepository.add(chat3);
        var expected = List.of(chat1, chat2, chat3);
        assertThat(chatRepository.findAll())
            .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Transactional
    @Rollback
    public void removeById() {
        Chat chat = new Chat(1L);
        chatRepository.add(chat);
        chatRepository.removeById(chat.id());
        assertThat(chatRepository.findById(chat.id()))
            .isEmpty();
        // when try to remove not existing chat not throws exception
        chatRepository.removeById(chat.id());
    }

    @Test
    @Transactional
    @Rollback
    public void existsById() {
        boolean exists = chatRepository.existsById(1L);
        assertThat(exists).isFalse();
        Chat chat = new Chat(1L);
        chatRepository.add(chat);
        exists = chatRepository.existsById(1L);
        assertThat(exists).isTrue();
    }
}
