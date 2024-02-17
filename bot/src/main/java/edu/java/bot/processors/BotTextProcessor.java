package edu.java.bot.processors;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class BotTextProcessor implements TextProcessor {

    private final MessageSource messageSource;

    @Autowired
    public BotTextProcessor(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String process(String messageKey) {
        return messageSource.getMessage(messageKey, null, Locale.of("ru"));
    }
}
