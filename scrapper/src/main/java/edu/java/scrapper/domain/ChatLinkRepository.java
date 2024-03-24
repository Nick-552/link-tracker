package edu.java.scrapper.domain;

import edu.java.scrapper.model.ChatLink;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ChatLinkRepository {

    @Transactional
    void add(Long chatId, Long linkId);

    @Transactional
    ChatLink remove(Long chatId, Long linkId);

    @Transactional
    void removeAllByChatId(Long chatId);

    @Transactional
    List<ChatLink> findAllByChatId(Long chatId);

    @Transactional
    List<ChatLink> findAllByLinkId(Long linkId);

    @Transactional
    boolean existsByChatIdAndLinkId(Long chatId, Long linkId);
}
