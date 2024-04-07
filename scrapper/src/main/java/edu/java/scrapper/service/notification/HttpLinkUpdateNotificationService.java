package edu.java.scrapper.service.notification;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.dto.request.bot.LinkUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpLinkUpdateNotificationService implements LinkUpdateNotificationService {

    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdate update) {
        botClient.sendUpdate(update);
    }
}
