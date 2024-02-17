package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartCommandTest {

    static StartCommand startCommand;

    static UsersLinksManager linkManager;

    static TextProcessor textProcessor;

    @Before
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        linkManager = Mockito.mock(UsersLinksManager.class);
        startCommand = new StartCommand(textProcessor, linkManager);
    }

    @Test
    public void startCommandTest() {
        Mockito.when(textProcessor.process("command.start.message")).thenReturn("start");
        Update update = createMockUpdate("/start", 1L);
        Mockito.when(update.message().chat().firstName()).thenReturn("username");
        SendMessage sendMessage = startCommand.handle(update);
        Mockito.verify(linkManager, Mockito.times(1)).registerUser("username", 1L);
        assertEquals("start", sendMessage.getParameters().get("text"));
    }
}

