package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpCommandTest {
    static HelpCommand helpCommand;

    static UsersLinksManager linkManager;

    static TextProcessor textProcessor;

    @Before
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        linkManager = Mockito.mock(UsersLinksManager.class);
        helpCommand = new HelpCommand(textProcessor, linkManager);
    }

    @Test
    public void helpCommandTest() {
        Mockito.when(textProcessor.process("command.help.show")).thenReturn("help");
        Update update = createMockUpdate("/help", 1L);
        SendMessage sendMessage = helpCommand.handle(update);
        assertEquals("help", sendMessage.getParameters().get("text"));
    }
}

