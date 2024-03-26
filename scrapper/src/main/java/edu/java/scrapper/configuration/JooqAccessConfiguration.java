package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.jooq.ChatLinkRepositoryJooqImpl;
import edu.java.scrapper.domain.repository.jooq.ChatRepositoryJooqImpl;
import edu.java.scrapper.domain.repository.jooq.LinkRepositoryJooqImpl;
import edu.java.scrapper.service.links.LinksService;
import edu.java.scrapper.service.links.LinksServiceRepoImpl;
import edu.java.scrapper.service.tgchats.TgChatsService;
import edu.java.scrapper.service.tgchats.TgChatsServiceRepoImpl;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import org.jooq.DSLContext;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public LinkRepositoryJooqImpl linkRepositoryJooqImpl(DSLContext context) {
        return new LinkRepositoryJooqImpl(context);
    }

    @Bean
    public ChatLinkRepositoryJooqImpl chatLinkRepositoryJooqImpl(DSLContext context) {
        return new ChatLinkRepositoryJooqImpl(context);
    }

    @Bean ChatRepositoryJooqImpl chatRepositoryJooqImpl(DSLContext context) {
        return new ChatRepositoryJooqImpl(context);
    }

    @Bean
    public TgChatsService tgChatsService(
        LinkRepositoryJooqImpl linkRepositoryJooqImpl,
        ChatLinkRepositoryJooqImpl chatLinkRepositoryJooqImpl,
        ChatRepositoryJooqImpl chatRepositoryJooqImpl
    ) {
        return new TgChatsServiceRepoImpl(
            linkRepositoryJooqImpl,
            chatLinkRepositoryJooqImpl,
            chatRepositoryJooqImpl
        );
    }

    @Bean
    public LinksService linksService(
        UpdateInfoServiceProvider updateInfoServiceProvider,
        LinkRepositoryJooqImpl linkRepositoryJooqImpl,
        ChatLinkRepositoryJooqImpl chatLinkRepositoryJooqImpl,
        ChatRepositoryJooqImpl chatRepositoryJooqImpl
    ) {
        return new LinksServiceRepoImpl(
            updateInfoServiceProvider,
            linkRepositoryJooqImpl,
            chatLinkRepositoryJooqImpl,
            chatRepositoryJooqImpl
        );
    }
}
