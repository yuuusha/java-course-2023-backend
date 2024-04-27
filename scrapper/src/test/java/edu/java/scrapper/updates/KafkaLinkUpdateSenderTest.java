package edu.java.scrapper.updates;

import edu.java.client.bot.dto.request.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.updates.sender.KafkaLinkUpdateSender;
import edu.java.updates.sender.LinkUpdateSender;
import edu.java.util.URLCreator;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class KafkaLinkUpdateSenderTest extends IntegrationEnvironment {

    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    @Autowired
    private ApplicationConfig config;

    @Test
    public void sendUpdateTest() {
        LinkUpdateSender linkUpdateSender = new KafkaLinkUpdateSender(kafkaTemplate, config);
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            URLCreator.createURL("https://github.com"),
            "github",
            List.of(1L),
            Map.of()
        );
        KafkaConsumer<String, LinkUpdate> kafkaConsumer = new KafkaConsumer<>(
                Map.of(
                        "bootstrap.servers", KAFKA.getBootstrapServers(),
                        "group.id", "scrapper",
                        "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
                        "value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer",
                        "properties.spring.json.trusted.packages", "*",
                        "spring.json.value.default.type", "edu.java.client.bot.dto.request.LinkUpdate",
                        "auto.offset.reset", "earliest"
                )
        );
        kafkaConsumer.subscribe(List.of(config.kafkaConfigurationInfo().topicName()));
        linkUpdateSender.sendUpdate(linkUpdate);
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                var records = kafkaConsumer.poll(Duration.ofMillis(100));
                Assertions.assertThat(records).hasSize(1);
                Assertions.assertThat(records.iterator().next().value()).isEqualTo(linkUpdate);
            });
    }
}
