package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.model.Chat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface TgChatsService {

    @Transactional
    void registerChat(Chat chat);

    @Transactional
    void deleteChat(Long id);
}
