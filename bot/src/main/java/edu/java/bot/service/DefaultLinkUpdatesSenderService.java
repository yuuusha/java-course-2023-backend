package edu.java.bot.service;

import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.Bot;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.processors.TextProcessor;
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
                    textProcessor.process("link.update")
                    )
                .disableWebPagePreview(true)
                    .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true))
            );
        });
    }
}
