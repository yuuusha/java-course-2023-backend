package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.dto.OptionalAnswer;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import edu.java.bot.service.URLService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.Assert.assertEquals;

public class UntrackCommandTest {

    static UntrackCommand untrackCommand;

    static BotService botService;

    static TextProcessor textProcessor;

    @BeforeEach
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        botService = Mockito.mock(BotService.class);
        untrackCommand = new UntrackCommand(textProcessor, botService);
    }

    @Test
    @DisplayName("Successful termination of tracking")
    public void handleSuccessfulUntrackedLinkTest() {
        Mockito.when(textProcessor.process("command.untrack.messages.successful_untrack", Map.of("link", "https://example.com")))
            .thenReturn("The link: https://example.com is now untraceable!");

        Mockito.when(botService.unTrackUserLink(1L, "https://example.com"))
            .thenReturn(OptionalAnswer.of(new LinkResponse(1L, URLService.createURL("https://example.com"))));
        Update message = createMockUpdate( "/untrack https://example.com", 1L);
        SendMessage sendMessage = untrackCommand.handle(message);
        assertEquals("The link: https://example.com is now untraceable!", sendMessage.getParameters().get("text"));
    }

    @Test
    @DisplayName("The link is not tracked")
    public void handleUntrackedLinkNotTrackedTest() {
        Mockito.when(textProcessor.process("command.untrack.messages.errors.not_tracked", Map.of("link", "https://example.com")))
            .thenReturn("The link: https://example.com is not tracked");

        Mockito.when(botService.unTrackUserLink(1L, "https://example.com"))
            .thenReturn(OptionalAnswer.error(new ApiErrorResponse("The link: https://example.com is not tracked.",
                null,
                null,
                null,
                null
            )));
        Update message = createMockUpdate("/untrack https://example.com", 1L);
        SendMessage sendMessage = untrackCommand.handle(message);
        assertEquals("The link: https://example.com is not tracked.", sendMessage.getParameters().get("text"));
    }

    @Test
    @DisplayName("Wrong argument")
    public void handleInvalidArgumentCommandTest() {
        Mockito.when(textProcessor.process("message.invalid_argument", Map.of("argument", "wefwe")))
            .thenReturn("Invalid argument: wefwe");

        Update message = createMockUpdate("/untrack wefwe", 1L);
        SendMessage sendMessage = untrackCommand.handle(message);
        assertEquals("Invalid argument: wefwe", sendMessage.getParameters().get("text"));
    }

    @Test
    @DisplayName("Empty argument")
    public void handleEmptyArgumentCommandTest() {
        Mockito.when(textProcessor.process("message.empty_argument"))
            .thenReturn("Empty argument");

        Update message = createMockUpdate("/untrack", 1L);
        SendMessage sendMessage = untrackCommand.handle(message);
        assertEquals("Empty argument", sendMessage.getParameters().get("text"));
    }
}
