package edu.java.updates.sender;

import edu.java.client.bot.dto.request.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
public class KafkaLinkUpdateSender implements LinkUpdateSender {

    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final ApplicationConfig config;

    @Override
    public void sendUpdate(LinkUpdate linkUpdate) {
        kafkaTemplate.send(config.kafkaConfigurationInfo().topicName(), linkUpdate);
    }
}
