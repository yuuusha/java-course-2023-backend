package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.BotMessageProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UpdateTrackingBotTest {
    @Test
    public void startTest() {
        TelegramBot bot = Mockito.mock(TelegramBot.class);
        MeterRegistry meterRegistry = Mockito.mock(MeterRegistry.class);
        UpdateTrackingBot updateTrackingBot =
            new UpdateTrackingBot(bot, Mockito.mock(BotMessageProcessor.class), meterRegistry);
        updateTrackingBot.start();
        Mockito.verify(bot, Mockito.times(1)).setUpdatesListener(Mockito.eq(updateTrackingBot));
    }

    @Test
    public void closeTest() {
        TelegramBot bot = Mockito.mock(TelegramBot.class);
        MeterRegistry meterRegistry = Mockito.mock(MeterRegistry.class);
        UpdateTrackingBot updateTrackingBot =
            new UpdateTrackingBot(bot, Mockito.mock(BotMessageProcessor.class), meterRegistry);
        updateTrackingBot.start();
        updateTrackingBot.close();
        Mockito.verify(bot, Mockito.times(1)).shutdown();
    }

    @Test
    public void executeNullTest() {
        MeterRegistry meterRegistry = Mockito.mock(MeterRegistry.class);
        UpdateTrackingBot updateTrackingBot =
            new UpdateTrackingBot(null, Mockito.mock(BotMessageProcessor.class), meterRegistry);
        assertThatThrownBy(updateTrackingBot::start).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void executeTest() {
        TelegramBot bot = Mockito.mock(TelegramBot.class);
        MeterRegistry meterRegistry = Mockito.mock(MeterRegistry.class);
        UpdateTrackingBot updateTrackingBot =
            new UpdateTrackingBot(bot, Mockito.mock(BotMessageProcessor.class), meterRegistry);
        SendMessage sendMessage = new SendMessage(1, "text");
        updateTrackingBot.execute(sendMessage);

        Mockito.verify(bot, Mockito.times(1)).execute(sendMessage);
    }
}
