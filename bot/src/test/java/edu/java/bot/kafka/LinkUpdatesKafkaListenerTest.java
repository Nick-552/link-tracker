package edu.java.bot.kafka;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkUpdateNotificationService;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeastOnce;

@SpringBootTest
@TestPropertySource(properties = {
    "SCRAPPER_API_CLIENT_BASE_URL=http://localhost:8080"
})
@DirtiesContext
class LinkUpdatesKafkaListenerTest extends KafkaIntegrationEnvironment {

    @MockBean
    private LinkUpdateNotificationService linkUpdateNotificationService;

    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private KafkaTemplate<String, String> wrongKafkaTemplate;

    private static final LinkUpdate LINK_UPDATE = new LinkUpdate(
        1L,
        URI.create("http://test.com"),
        "",
        "",
        List.of(1L)
    );


    @Test
    public void updateLinks_whenEverythingIsOk_shouldReceiveMessageAndProcessIt() {
        kafkaTemplate.send("linkUpdate", LINK_UPDATE);
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> Mockito.verify(linkUpdateNotificationService)
                .notifyAllWithLinkUpdate(LINK_UPDATE));
    }

    @Test
    public void updateLinks_whenExceptionDuringHandling_shouldSendToDlq() {
        kafkaTemplate.send("linkUpdate", LINK_UPDATE);
        Mockito
            .doThrow(new RuntimeException())
            .when(linkUpdateNotificationService)
            .notifyAllWithLinkUpdate(LINK_UPDATE);
        KafkaConsumer<String, LinkUpdate> dlqKafkaConsumer = new KafkaConsumer<>(
                kafkaProperties.buildConsumerProperties(null)
        );
        dlqKafkaConsumer.subscribe(List.of("linkUpdate_dlq"));
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(20))
            .untilAsserted(() -> {
                var values = dlqKafkaConsumer.poll(Duration.ofMillis(100));
                assertThat(values).hasSize(1);
                assertThat(values.iterator().next().value()).isEqualTo(LINK_UPDATE);
                Mockito.verify(linkUpdateNotificationService, atLeastOnce())
                    .notifyAllWithLinkUpdate(LINK_UPDATE);
            });
    }

    @Test
    public void updateLinks_whenWrongMessage_shouldSendToDlq() {
        wrongKafkaTemplate.send("linkUpdate", "wrong");
        var linkUpdate = new LinkUpdate(
            1L,
            URI.create("http://test.com"),
            "",
            "!!! Update after wrong message !!!",
            List.of(1L));
        kafkaTemplate.send("linkUpdate", linkUpdate);
        KafkaConsumer<String, LinkUpdate> dlqKafkaConsumer = new KafkaConsumer<>(
            kafkaProperties.buildConsumerProperties(null)
        );
        dlqKafkaConsumer.subscribe(List.of("linkUpdate_dlq"));
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(20))
            .untilAsserted(() -> {
                Mockito.verify(linkUpdateNotificationService).notifyAllWithLinkUpdate(linkUpdate);
            });
    }
}
