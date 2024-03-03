package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LinkUpdateNotificationService {

    public void notifyAllWithLinkUpdate(LinkUpdate linkUpdate) {
        for (var chatId: linkUpdate.tgChatIds()) {
            log.info(
                "notifying to chatId {} that link {} was updated: {} ",
                chatId, linkUpdate.url(), linkUpdate.description()
            );
        }
    }
}
