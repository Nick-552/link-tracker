package edu.java.scrapper.kafka;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.request.bot.LinkUpdate;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
class ScrapperQueueProducerTest extends IntegrationEnvironment {

    @Autowired
    private ScrapperQueueProducer scrapperQueueProducer;

    @Autowired
    private ApplicationConfig config;

    @Test
    public void sendNotification_shouldWorkCorrectly() {
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            URI.create("http://test.com"),
            "",
            "",
            List.of(1L)
        );
        var kafkaConsumer = new KafkaConsumer<String, LinkUpdate>(
            Map.of(
                "group.id", "scrapper",
                "bootstrap.servers", KAFKA_CONTAINER.getBootstrapServers(),
                "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer",
                "properties.spring.json.trusted.packages", "*",
                "spring.json.value.default.type", "edu.java.scrapper.dto.request.bot.LinkUpdate",
                "auto.offset.reset", "earliest"
            )
        );
        kafkaConsumer.subscribe(List.of(config.kafkaTopics().linkUpdate()));
        scrapperQueueProducer.sendMessage(linkUpdate);
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                var records = kafkaConsumer.poll(Duration.ofMillis(100));
                assertThat(records).hasSize(1);
                assertThat(records.iterator().next().value()).isEqualTo(linkUpdate);
            });
    }
}
