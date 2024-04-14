package edu.java.scrapper.service.notification;

import edu.java.scrapper.dto.request.bot.LinkUpdate;

public interface LinkUpdateNotificationService {

    void sendUpdate(LinkUpdate update);
}
