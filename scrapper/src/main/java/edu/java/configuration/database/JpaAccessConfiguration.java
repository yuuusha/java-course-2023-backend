package edu.java.configuration.database;

import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.service.LinkService;
import edu.java.service.TelegramChatService;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.supplier.InfoSuppliers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService jpaLinkService(
        JpaLinkRepository jpaLinkRepository,
        JpaChatRepository jpaChatRepository,
        InfoSuppliers infoSuppliers
    ) {
        return new JpaLinkService(jpaLinkRepository, jpaChatRepository, infoSuppliers);
    }

    @Bean
    public TelegramChatService jpaChatService(
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
        return new JpaChatService(jpaChatRepository, jpaLinkRepository);
    }
}
