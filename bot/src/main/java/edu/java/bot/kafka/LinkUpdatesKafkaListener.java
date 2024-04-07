package edu.java.bot.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkUpdateNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LinkUpdatesKafkaListener {

    private final ApplicationConfig config;

    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    private final LinkUpdateNotificationService linkUpdateNotificationService;

    @KafkaListener(topics = "${app.kafka-topics.link-update}", groupId = "link-updates")
    public void listenLinkUpdates(LinkUpdate linkUpdate) {
        try {
            linkUpdateNotificationService.notifyAllWithLinkUpdate(linkUpdate);
        } catch (Exception e) {
            var dlqTopic = config.kafkaTopics().linkUpdate() + config.kafkaTopics().dlqSuffix();
            kafkaTemplate.send(dlqTopic, linkUpdate);
        }
    }
}
