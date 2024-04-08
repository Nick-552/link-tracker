package edu.java.bot.kafka;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkUpdateNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class LinkUpdatesKafkaListener {

    private final LinkUpdateNotificationService linkUpdateNotificationService;

    @KafkaListener(topics = "${app.kafka-topics.link-update}", groupId = "link-updates")
    @RetryableTopic(
        autoCreateTopics = "false",
        dltTopicSuffix = "${app.kafka-topics.dlq-suffix}",
        dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    public void listenLinkUpdates(LinkUpdate linkUpdate) {
        linkUpdateNotificationService.notifyAllWithLinkUpdate(linkUpdate);
    }

    @DltHandler
    public void handleError(LinkUpdate linkUpdate) {
        log.warn("Ошибка обработки сообщения {}. Отправлено в очередь плохих сообщений", linkUpdate);
    }
}
