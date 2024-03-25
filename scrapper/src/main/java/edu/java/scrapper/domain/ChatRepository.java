package edu.java.scrapper.domain;

import edu.java.scrapper.model.Chat;
import java.util.List;

public interface ChatRepository {

    void add(Chat chat);

    void removeById(Long id);

    List<Chat> findAll();

    Chat findById(Long id);

    boolean existsById(Long id);
}
