package edu.java.bot.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkUpdatesSenderService;
import edu.java.bot.util.URLCreator;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class KafkaUpdatesListenerTest extends KafkaIntegrationEnvironment {

    @MockBean
    private LinkUpdatesSenderService linkUpdatesSenderService;

    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Test
    public void listenUpdateCorrectTest() {
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            URLCreator.createURL("https://github.com"),
            "github",
            List.of(1L),
            Map.of()
        );
        kafkaTemplate.send("updates", linkUpdate);
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> Mockito.verify(linkUpdatesSenderService, Mockito.times(1))
                .sendLinkUpdate(linkUpdate));
    }

    @Test
    public void listenUpdateInCorrectTest() {
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            URLCreator.createURL("https://github.com"),
            "github",
            List.of(1L),
            Map.of()
        );
        Mockito.doThrow(RuntimeException.class).when(linkUpdatesSenderService).sendLinkUpdate(linkUpdate);
        KafkaConsumer<String, LinkUpdate> dlqKafkaConsumer = new KafkaConsumer<>(
            kafkaProperties.buildConsumerProperties(null)
        );
        dlqKafkaConsumer.subscribe(List.of(config.kafkaConfigurationInfo().topicName() + "_dlq"));
        kafkaTemplate.send(config.kafkaConfigurationInfo().topicName(), linkUpdate);
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(10))
            .untilAsserted(() -> {
                var values = dlqKafkaConsumer.poll(Duration.ofMillis(100));
                Assertions.assertThat(values).hasSize(1);
                Assertions.assertThat(values.iterator().next().value()).isEqualTo(linkUpdate);
                Mockito.verify(linkUpdatesSenderService).sendLinkUpdate(linkUpdate);
            });
    }
}
