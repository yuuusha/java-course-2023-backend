package edu.java.bot.service;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.Bot;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.processors.TextProcessor;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class DefaultLinkUpdatesSenderService implements LinkUpdatesSenderService {

    private final Bot bot;
    private final TextProcessor textProcessor;

    @Override
    public void sendLinkUpdate(LinkUpdate link) {
        link.tgChatIds().forEach(chatId -> {
            bot.execute(
                new SendMessage(
                    chatId,
                    textProcessor.process("link.update", Map.of("link", String.valueOf(link.url()), "description",
                        textProcessor.process(
                            link.description(),
                            link.metaInfo(),
                            "Default update"
                        )))
                ).parseMode(ParseMode.Markdown));
        });
    }
}
