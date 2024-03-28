package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import edu.java.scrapper.service.links.LinksService;
import edu.java.scrapper.service.links.LinksServiceJpaImpl;
import edu.java.scrapper.service.tgchats.TgChatsService;
import edu.java.scrapper.service.tgchats.TgChatsServiceJpaImpl;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinksService linksService(
        UpdateInfoServiceProvider updateInfoServiceProvider,
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
        return new LinksServiceJpaImpl(
            updateInfoServiceProvider,
            jpaChatRepository,
            jpaLinkRepository
        );
    }

    @Bean
    TgChatsService tgChatsService(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
        return new TgChatsServiceJpaImpl(
            jpaChatRepository,
            jpaLinkRepository
        );
    }
}
