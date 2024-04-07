package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.service.kafka.ScrapperQueueProducer;
import edu.java.scrapper.service.notification.HttpLinkUpdateNotificationService;
import edu.java.scrapper.service.notification.KafkaLinkUpdateNotificationService;
import edu.java.scrapper.service.notification.LinkUpdateNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkUpdateNotificationConfig {

    @Bean
    public LinkUpdateNotificationService linkUpdateNotificationService(
        ApplicationConfig config,
        BotClient botClient,
        ScrapperQueueProducer scrapperQueueProducer
    ) {
        if (config.useQueue()) {
            return new KafkaLinkUpdateNotificationService(scrapperQueueProducer);
        }
        return new HttpLinkUpdateNotificationService(botClient);
    }
}
