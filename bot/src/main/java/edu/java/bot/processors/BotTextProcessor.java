package edu.java.bot.processors;

import java.util.Locale;
import java.util.Map;
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
    public String process(String option, Map<String, String> keyWords) {
        String message = resourceBundleMessageSource.getMessage(option, null, Locale.of("ru"));
        return ReplacerValiables.replaceVariables(message, keyWords);
    }

    @Override
    public String process(String option, Map<String, String> keyWords, String defaultValue) {
        String handledMessage = process(option, keyWords);
        if (handledMessage.equals(option)) {
            return defaultValue;
        }
        return handledMessage;
    }
}
