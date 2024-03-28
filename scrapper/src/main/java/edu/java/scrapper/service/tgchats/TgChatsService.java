package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.model.Chat;
import org.springframework.stereotype.Service;

@Service
public interface TgChatsService {

    void registerChat(Chat chat);

    void deleteChat(Long id);
}
