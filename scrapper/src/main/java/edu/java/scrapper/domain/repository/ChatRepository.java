package edu.java.scrapper.domain.repository;

import edu.java.scrapper.model.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    void add(Chat chat);

    void removeById(Long id);

    List<Chat> findAll();

    Optional<Chat> findById(Long id);

    boolean existsById(Long id);
}
