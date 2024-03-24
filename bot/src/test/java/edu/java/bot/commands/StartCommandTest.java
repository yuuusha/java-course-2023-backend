package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.Assert.assertEquals;

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
    public void handleRegisterUserTest() {
        Mockito.when(textProcessor.process("command.start.messages.success.first_hello_message")).thenReturn("Hello");
        Update message = createMockUpdate("/start", 1L);
        SendMessage sendMessage = startCommand.handle(message);
        Mockito.verify(botService, Mockito.times(1)).registerUserIfNew(1L);
        assertEquals("Hello", sendMessage.getParameters().get("text"));
    }
}
