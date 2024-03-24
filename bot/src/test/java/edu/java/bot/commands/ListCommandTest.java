package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.List;

import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.Assert.assertEquals;

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
    @DisplayName("No tracked links")
    public void handleReturnEmptyListMessageTest() {
        Mockito.when(textProcessor.process("command.list.messages.empty_list_of_links")).thenReturn("You haven't any tracked links");
        Update message = createMockUpdate("/list", 1L);
        Mockito.when(botService.userLinks(1L))
            .thenReturn(new OptionalAnswer<>(new ListLinksResponse(List.of()), null));
        SendMessage sendMessage = listCommand.handle(message);
        assertEquals("You haven't any tracked links", sendMessage.getParameters().get("text"));
    }

    @SneakyThrows @Test
    @DisplayName("Displaying tracked links")
    public void handleReturnNotEmptyListMessageTest() {
        Mockito.when(textProcessor.process("command.list.messages.show_tracked_links")).thenReturn("Your tracked links:\n");
        Mockito.when(botService.userLinks(1L))
            .thenReturn(new OptionalAnswer<>(new ListLinksResponse(List.of(
                new LinkResponse(1L, URI.create("http://example.com").toURL()),
                new LinkResponse(1L, URI.create("https://example.com").toURL())
            )), null));

        Update message = createMockUpdate("/list", 1L);
        SendMessage sendMessage = listCommand.handle(message);

        assertEquals(
            "Your tracked links:\n1. http://example.com\n2. https://example.com\n",
                sendMessage.getParameters().get("text")
        );
        Mockito.verify(botService, Mockito.times(2)).userLinks(1L);
    }
}
