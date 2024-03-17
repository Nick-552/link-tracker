package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.dto.request.LinkUpdate;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import static edu.java.bot.utils.BotUtils.createUpdateMessage;

@Service
@Log4j2
@RequiredArgsConstructor
public class LinkUpdateNotificationService {

    private final TelegramBot telegramBot;

    public void notifyAllWithLinkUpdate(LinkUpdate linkUpdate) {
        log.info("notifying all with link update: {}", linkUpdate);
        var url = linkUpdate.url();
        var updatedAt = linkUpdate.updatedAt();
        var description = linkUpdate.description();
        for (var chatId: linkUpdate.tgChatIds()) {
            log.info(
                "notifying to chatId {} that link {} was updated: {} ",
                chatId, linkUpdate.url(), linkUpdate.description()
            );
            telegramBot.execute(
                createUpdateMessage(chatId, url, OffsetDateTime.parse(updatedAt), description)
            );
        }
    }
}
