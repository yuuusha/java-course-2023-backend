package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.Link;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UntrackCommandTest {

    static UntrackCommand untrackCommand;

    static UsersLinksManager linkManager;

    static TextProcessor textProcessor;

    @Before
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        linkManager = Mockito.mock(UsersLinksManager.class);
        untrackCommand = new UntrackCommand(textProcessor, linkManager);
    }

    @Test
    public void untrackCommandSuccessTest() {
        Mockito.when(textProcessor.process("command.untrack.success")).thenReturn("success");
        Mockito.when(linkManager.linkExist("https://edu.tinkoff.ru", 1L)).thenReturn(true);
        Update update = createMockUpdate("/untrack https://edu.tinkoff.ru", 1L);
        SendMessage sendMessage = untrackCommand.handle(update);
        assertEquals("success", sendMessage.getParameters().get("text"));
    }

    @Test
    public void untrackCommandUnsuccessTest() {
        Mockito.when(textProcessor.process("command.untrack.unsuccess")).thenReturn("unsuccess");
        Update update = createMockUpdate("/untrack abcdefgh", 1L);
        SendMessage sendMessage = untrackCommand.handle(update);
        assertEquals("unsuccess", sendMessage.getParameters().get("text"));
    }
}

