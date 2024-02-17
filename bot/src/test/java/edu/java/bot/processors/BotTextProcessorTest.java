package edu.java.bot.processors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static edu.java.bot.Utils.messageSource;

public class BotTextProcessorTest {

    @Test
    public void textProcessorTest() {
        TextProcessor textProcessor = new BotTextProcessor(messageSource());
        String name = textProcessor.process("name");
        Assertions.assertEquals("Ivan", name);
    }
}
