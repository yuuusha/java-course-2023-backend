package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpCommandTest {
    static HelpCommand helpCommand;

    static BotService botService;

    static TextProcessor textProcessor;

    @BeforeEach
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        botService = Mockito.mock(BotService.class);
        helpCommand = new HelpCommand(textProcessor, botService);
    }

    @Test
    public void helpCommandTest() {
        Mockito.when(textProcessor.process("command.help.show")).thenReturn("help");
        Update update = createMockUpdate("/help", 1L);
        SendMessage sendMessage = helpCommand.handle(update);
        assertEquals("help", sendMessage.getParameters().get("text"));
    }
}

