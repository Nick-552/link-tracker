package edu.java.scrapper.service.notification;

import edu.java.scrapper.dto.request.bot.LinkUpdate;
import edu.java.scrapper.kafka.ScrapperQueueProducer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KafkaLinkUpdateNotificationService implements LinkUpdateNotificationService {

    private final ScrapperQueueProducer scrapperQueueProducer;

    @Override
    public void sendUpdate(LinkUpdate update) {
        scrapperQueueProducer.sendMessage(update);
    }
}
