package edu.java.configuration.database;

import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.service.LinkService;
import edu.java.service.TelegramChatService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcTelegramChatService;
import edu.java.supplier.InfoSuppliers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService jdbcLinkService(
        JdbcChatLinkRepository chatLinkRepository,
        JdbcLinkRepository linkRepository,
        InfoSuppliers infoSuppliers
    ) {
        return new JdbcLinkService(chatLinkRepository, linkRepository, infoSuppliers);
    }

    @Bean
    public TelegramChatService jdbcChatService(
        JdbcChatRepository chatRepository,
        JdbcChatLinkRepository chatLinkRepository,
        JdbcLinkRepository linkRepository
    ) {
        return new JdbcTelegramChatService(chatRepository, chatLinkRepository, linkRepository);
    }
}
