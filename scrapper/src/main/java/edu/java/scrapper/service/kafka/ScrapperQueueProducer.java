package edu.java.scrapper.service.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.request.bot.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {

    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate; // <key, value>

    private final ApplicationConfig applicationConfig;

    private static final String LINK_UPDATE_TOPIC = "link-update";


    public void sendMessage(LinkUpdate update) {
        kafkaTemplate.send(applicationConfig.kafkaTopics().get(LINK_UPDATE_TOPIC), update);
    }
}
