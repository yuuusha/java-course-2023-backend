package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.Link;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.List;
import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {
    static ListCommand listCommand;

    static UsersLinksManager linkManager;

    static TextProcessor textProcessor;

    @Before
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        linkManager = Mockito.mock(UsersLinksManager.class);
        listCommand = new ListCommand(textProcessor, linkManager);
    }

    @Test
    public void emptyListCommandTest() {
        Mockito.when(textProcessor.process("command.list.empty")).thenReturn("empty");

        Update update = createMockUpdate("/list", 1L);
        SendMessage sendMessage = listCommand.handle(update);
        assertEquals("empty", sendMessage.getParameters().get("text"));
    }

    @Test
    public void listCommandTest() {
        Mockito.when(textProcessor.process("command.list.get")).thenReturn("%s");
        Mockito.when(linkManager.getListLinks(1L))
            .thenReturn(List.of(new Link("https://edu.tinkoff.ru")));

        Update update = createMockUpdate("/list", 1L);
        SendMessage sendMessage = listCommand.handle(update);

        assertEquals("\n1. https://edu.tinkoff.ru", sendMessage.getParameters().get("text")
        );
    }
}
