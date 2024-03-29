package edu.java.scrapper.domain.repository;

import edu.java.scrapper.model.ChatLink;
import java.util.List;
import java.util.Optional;

public interface ChatLinkRepository {

    void add(Long chatId, Long linkId);

    Optional<ChatLink> remove(Long chatId, Long linkId);

    void removeAllByChatId(Long chatId);

    List<ChatLink> findAllByChatId(Long chatId);

    List<ChatLink> findAllByLinkId(Long linkId);

    boolean existsByChatIdAndLinkId(Long chatId, Long linkId);
}
