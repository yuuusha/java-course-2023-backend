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

public class StartCommandTest {

    static StartCommand startCommand;

    static BotService botService;

    static TextProcessor textProcessor;

    @BeforeEach
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        botService = Mockito.mock(BotService.class);
        startCommand = new StartCommand(textProcessor, botService);
    }

    @Test
    public void startCommandTest() {
        Mockito.when(textProcessor.process("command.start.message")).thenReturn("start");
        Update update = createMockUpdate("/start", 1L);
        Mockito.when(update.message().chat().firstName()).thenReturn("username");
        SendMessage sendMessage = startCommand.handle(update);
        Mockito.verify(botService, Mockito.times(1)).registerUser(1L);
        assertEquals("start", sendMessage.getParameters().get("text"));
    }
}

