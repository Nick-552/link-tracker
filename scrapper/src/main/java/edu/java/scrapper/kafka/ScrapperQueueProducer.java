package edu.java.scrapper.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.request.bot.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {

    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    private final ApplicationConfig applicationConfig;

    public void sendMessage(LinkUpdate update) {
        kafkaTemplate.send(applicationConfig.kafkaTopics().linkUpdate(), update);
    }
}
