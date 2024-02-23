package edu.java.bot.processors;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class BotTextProcessor implements TextProcessor {

    private final ResourceBundleMessageSource resourceBundleMessageSource;

    @Autowired
    public BotTextProcessor(ResourceBundleMessageSource resourceBundleMessageSource) {
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    @Override
    public String process(String messageKey) {
        return resourceBundleMessageSource.getMessage(messageKey, null, Locale.of("ru"));
    }
}
