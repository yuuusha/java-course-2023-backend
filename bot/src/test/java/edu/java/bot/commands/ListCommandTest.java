package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.net.URI;
import java.util.List;
import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {
    static ListCommand listCommand;

    static BotService botService;

    static TextProcessor textProcessor;

    @BeforeEach
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        botService = Mockito.mock(BotService.class);
        listCommand = new ListCommand(textProcessor, botService);
    }

    @Test
    public void emptyListCommandTest() {
        Mockito.when(textProcessor.process("command.list.empty")).thenReturn("empty");
        Mockito.when(botService.getListLinks(1L)).thenReturn(new ListLinksResponse(List.of()));
        Update update = createMockUpdate("/list", 1L);
        SendMessage sendMessage = listCommand.handle(update);
        assertEquals("empty", sendMessage.getParameters().get("text"));
    }

    @SneakyThrows
    @Test
    public void listCommandTest() {
        Mockito.when(textProcessor.process("command.list.get")).thenReturn("%s");
        Mockito.when(botService.getListLinks(1L))
            .thenReturn(new ListLinksResponse(List.of(
                new LinkResponse(1L, URI.create("https://edu.tinkoff.ru").toURL())
            )));

        Update update = createMockUpdate("/list", 1L);
        SendMessage sendMessage = listCommand.handle(update);

        assertEquals("\n1. https://edu.tinkoff.ru", sendMessage.getParameters().get("text")
        );
    }
}
