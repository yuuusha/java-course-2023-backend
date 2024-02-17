package edu.java.bot.processors;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static edu.java.bot.Utils.messageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class BotTextProcessorTest {

    @Test
    public void textProcessorTest() {
        TextProcessor textProcessor = new BotTextProcessor(messageSource());
        String name = textProcessor.process("name");
        Assertions.assertEquals("Ivan", name);
    }
}
