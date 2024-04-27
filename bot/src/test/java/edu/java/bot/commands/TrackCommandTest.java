package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyDescription;
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

public class TrackCommandTest {

    static TrackCommand trackCommand;

    static BotService botService;

    static TextProcessor textProcessor;

    @BeforeEach
    public void setUp() {
        textProcessor = Mockito.mock(TextProcessor.class);
        botService = Mockito.mock(BotService.class);
        trackCommand = new TrackCommand(textProcessor, botService);
    }

    @Test
    @DisplayName("Successful tracking")
    public void handleSuccessfulTrackLinkTest() {
        Mockito.when(textProcessor.process("command.track.messages.successful_track", Map.of("link", "https://example.com")))
            .thenReturn("The link: https://example.com is now being tracked");

        Mockito.when(botService.trackUserLink(1L, "https://example.com"))
            .thenReturn(OptionalAnswer.of(new LinkResponse(1L, URLService.createURL("https://example.com"))));
        Update message = createMockUpdate("/track https://example.com", 1L);
        SendMessage sendMessage = trackCommand.handle(message);
        assertEquals("The link: https://example.com is now being tracked", sendMessage.getParameters().get("text"));
    }

    @Test
    @DisplayName("The link is already being tracked")
    public void handleAlreadyTrackLinkTest() {
        Mockito.when(textProcessor.process("command.track.messages.errors.already_tracked", Map.of("link", "https://example.com")))
            .thenReturn("The link: https://example.com is already being tracked.");

        Mockito.when(botService.trackUserLink(1L, "https://example.com"))
            .thenReturn(OptionalAnswer.error(new ApiErrorResponse("The link: https://example.com is already being tracked.",
                null,
                null,
                null,
                null
            )));
        Update message = createMockUpdate("/track https://example.com", 1L);
        SendMessage sendMessage = trackCommand.handle(message);
        assertEquals("The link: https://example.com is already being tracked.", sendMessage.getParameters().get("text"));
    }

    @Test
    @DisplayName("Wrong argument")
    public void handleInvalidArgumentCommandTest() {
        Mockito.when(textProcessor.process("message.invalid_argument"))
            .thenReturn("Invalid argument: %s");

        Update message = createMockUpdate("/track wefwe", 1L);
        SendMessage sendMessage = trackCommand.handle(message);
        assertEquals("Invalid argument: wefwe", sendMessage.getParameters().get("text"));
    }

    @Test
    @DisplayName("Empty argument")
    public void handleEmptyArgumentCommandTest() {
        Mockito.when(textProcessor.process("message.empty_argument"))
            .thenReturn("Empty argument");

        Update message = createMockUpdate("/track", 1L);
        SendMessage sendMessage = trackCommand.handle(message);
        assertEquals("Empty argument", sendMessage.getParameters().get("text"));
    }
}
