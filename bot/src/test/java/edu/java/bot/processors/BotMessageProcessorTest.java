package edu.java.bot.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static edu.java.bot.Utils.createMockUpdate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BotMessageProcessorTest {

    private static BotMessageProcessor botProcessor;
    Command command;

    @BeforeEach
    public void setUp() {
        int chatId = 1;

        TextProcessor textProcessor = Mockito.mock(TextProcessor.class);
        Mockito.when(textProcessor.process(Mockito.anyString())).thenReturn("unknown command");

        command = Mockito.mock(Command.class);
        Mockito.when(command.command()).thenReturn("/command");
        Mockito.when(command.handle(Mockito.any())).thenReturn(new SendMessage(chatId, "message"));

        botProcessor = new BotMessageProcessor(List.of(command), textProcessor);
    }

    @Test
    public void processMessageNullTest() {
        assertNull(botProcessor.process(Mockito.mock(Update.class)));
    }

    @Test
    public void processMessageTextNullTest() {
        assertNull(botProcessor.process(createMockUpdate(null, 1L)));
    }

    @Test
    public void commandsTest() {
        assertTrue(botProcessor.commands().stream().allMatch(command -> command.command().equals("/command")));
    }

    @Test
    public void CommandWithCorrectParameterTest() {
        assertThat(botProcessor.process(createMockUpdate("/command", 1L)).getParameters().get("text")).isEqualTo(
            "message");
    }

    @Test
    public void updateNullTest() {
        assertNull(botProcessor.process(new Update()));
    }

    @Test
    public void updateWithUnknownMessageTest() {
        assertEquals("unknown command", botProcessor.process(createMockUpdate("/unknownCommand", 1L)).getParameters().get("text"));
    }
}

