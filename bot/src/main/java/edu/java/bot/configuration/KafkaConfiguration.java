package edu.java.bot.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic topic(ApplicationConfig config) {
        return TopicBuilder.name(config.kafkaConfigurationInfo().topicName() + "_dlq")
            .partitions(1)
            .replicas(1)
            .build();
    }
}
