package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaUpdatesListener {
    private final LinkUpdatesSenderService senderService;

    @KafkaListener(topics = "${app.kafka-configuration-info.topic-name}", groupId = "bot")
    @RetryableTopic(attempts = "1", dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "_dlq")
    public void listenUpdates(LinkUpdate linkUpdate) {
        senderService.sendLinkUpdate(linkUpdate);
    }
}
