package edu.java.configuration.database;

import edu.java.repository.jooq.JooqChatLinkRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.service.LinkService;
import edu.java.service.TelegramChatService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcTelegramChatService;
import edu.java.supplier.InfoSuppliers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {

    @Bean
    public LinkService jooqLinkService(
        JooqChatLinkRepository chatLinkRepository,
        JooqLinkRepository linkRepository,
        InfoSuppliers infoSuppliers
    ) {
        return new JdbcLinkService(chatLinkRepository, linkRepository, infoSuppliers);
    }

    @Bean
    public TelegramChatService jooqChatService(
        JooqChatRepository chatRepository,
        JooqChatLinkRepository chatLinkRepository,
        JooqLinkRepository linkRepository
    ) {
        return new JdbcTelegramChatService(chatRepository, chatLinkRepository, linkRepository);
    }
}
