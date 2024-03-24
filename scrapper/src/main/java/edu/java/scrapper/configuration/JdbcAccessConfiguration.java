package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.jdbc.ChatLinkRepositoryJdbcImpl;
import edu.java.scrapper.domain.jdbc.ChatRepositoryJdbcImpl;
import edu.java.scrapper.domain.jdbc.LinkRepositoryJdbcImpl;
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
    public ChatRepositoryJdbcImpl chatRepository(JdbcClient jdbcClient) {
        return new ChatRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public ChatLinkRepositoryJdbcImpl chatLinkRepository(JdbcClient jdbcClient) {
        return new ChatLinkRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public LinkRepositoryJdbcImpl linkRepository(JdbcClient jdbcClient) {
        return new LinkRepositoryJdbcImpl(jdbcClient);
    }

    @Bean
    public TgChatsService tgChatsService(
        LinkRepositoryJdbcImpl linkRepositoryJdbcImpl,
        ChatLinkRepositoryJdbcImpl chatLinkRepositoryJdbcImpl,
        ChatRepositoryJdbcImpl chatRepositoryJdbcImpl
    ) {
        return new TgChatsServiceRepoImpl(
            linkRepositoryJdbcImpl,
            chatLinkRepositoryJdbcImpl,
            chatRepositoryJdbcImpl
        );
    }

    @Bean
    public LinksService linksService(
        UpdateInfoServiceProvider updateInfoServiceProvider,
        LinkRepositoryJdbcImpl linkRepositoryJdbcImpl,
        ChatLinkRepositoryJdbcImpl chatLinkRepositoryJdbcImpl,
        ChatRepositoryJdbcImpl chatRepositoryJdbcImpl
    ) {
        return new LinksServiceRepoImpl(
            updateInfoServiceProvider,
            linkRepositoryJdbcImpl,
            chatLinkRepositoryJdbcImpl,
            chatRepositoryJdbcImpl
        );
    }
}
