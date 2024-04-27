package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.Utils.createMockUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void untrackCommandSuccessTest() {
        Mockito.when(textProcessor.process("command.untrack.success")).thenReturn("success");
        Mockito.when(botService.isLinkExist(1L, "https://edu.tinkoff.ru")).thenReturn(true);
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

