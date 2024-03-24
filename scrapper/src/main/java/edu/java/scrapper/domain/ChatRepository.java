package edu.java.scrapper.domain;

import edu.java.scrapper.model.Chat;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRepository {

    @Transactional
    void add(Chat chat);

    @Transactional
    void removeById(Long id);

    @Transactional
    List<Chat> findAll();

    @Transactional
    Chat findById(Long id);

    @Transactional
    boolean existsById(Long id);
}
