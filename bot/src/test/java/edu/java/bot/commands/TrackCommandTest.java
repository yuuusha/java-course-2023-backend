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

public class TrackCommandTest {

    static TrackCommand trackCommand;

    static UsersLinksManager linkManager;

    static TextProcessor textProcessor;

    @Before
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        linkManager = Mockito.mock(UsersLinksManager.class);
        trackCommand = new TrackCommand(textProcessor, linkManager);
    }

    @Test
    public void trackCommandSuccessTest() {
        Mockito.when(textProcessor.process("command.track.success")).thenReturn("success");
        Update update = createMockUpdate("/track https://edu.tinkoff.ru", 1L);
        SendMessage sendMessage = trackCommand.handle(update);
        assertEquals("success", sendMessage.getParameters().get("text"));
    }

    @Test
    public void trackCommandExistTest() {
        Mockito.when(textProcessor.process("command.track.exist")).thenReturn("exist");
        Mockito.when(linkManager.linkExist("https://edu.tinkoff.ru", 1L)).thenReturn(true);
        Update update = createMockUpdate("/track https://edu.tinkoff.ru", 1L);
        SendMessage sendMessage = trackCommand.handle(update);
        assertEquals("exist", sendMessage.getParameters().get("text"));
    }

    @Test
    public void trackCommandUnsuccessTest() {
        Mockito.when(textProcessor.process("command.track.unsuccess")).thenReturn("unsuccess");

        Update update = createMockUpdate("/track abcdefgh", 1L);
        SendMessage sendMessage = trackCommand.handle(update);
        assertEquals("unsuccess", sendMessage.getParameters().get("text"));
    }
}

