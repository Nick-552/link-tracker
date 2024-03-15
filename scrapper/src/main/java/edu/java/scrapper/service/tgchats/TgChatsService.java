package edu.java.scrapper.service.tgchats;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface TgChatsService {

    @Transactional
    void registerChat(Long id);

    @Transactional
    void deleteChat(Long id);
}
