package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.jdbc.ChatLinkRepositoryJdbcImpl;
import edu.java.scrapper.domain.repository.jdbc.ChatRepositoryJdbcImpl;
import edu.java.scrapper.domain.repository.jdbc.LinkRepositoryJdbcImpl;
import edu.java.scrapper.service.links.LinksService;
import edu.java.scrapper.service.links.LinksServiceRepoImpl;
import edu.java.scrapper.service.tgchats.TgChatsService;
import edu.java.scrapper.service.tgchats.TgChatsServiceRepoImpl;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public ChatRepository chatRepository(JdbcClient jdbcClient) {
        return new ChatRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public ChatLinkRepository chatLinkRepository(JdbcClient jdbcClient) {
        return new ChatLinkRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public LinkRepository linkRepository(JdbcClient jdbcClient) {
        return new LinkRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public TgChatsService tgChatsService(
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        ChatRepository chatRepository
    ) {
        return new TgChatsServiceRepoImpl(
            linkRepository,
            chatLinkRepository,
            chatRepository
        );
    }

    @Bean
    public LinksService linksService(
        UpdateInfoServiceProvider updateInfoServiceProvider,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        ChatRepository chatRepository
    ) {
        return new LinksServiceRepoImpl(
            updateInfoServiceProvider,
            linkRepository,
            chatLinkRepository,
            chatRepository
        );
    }
}
