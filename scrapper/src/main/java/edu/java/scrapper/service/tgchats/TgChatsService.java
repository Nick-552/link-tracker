package edu.java.scrapper.service.tgchats;

import org.springframework.stereotype.Service;

@Service
public interface TgChatsService {

    void registerChat(Long id);

    void deleteChat(Long id);
}
