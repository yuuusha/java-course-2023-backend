package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.UpdateTrackingBot;
import edu.java.bot.processors.BotMessageProcessor;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class UpdateTrackingBotTest {
    @Test
    public void startTest() {
        TelegramBot bot = Mockito.mock(TelegramBot.class);
        UpdateTrackingBot updateTrackingBot =
            new UpdateTrackingBot(Mockito.mock(BotMessageProcessor.class), bot);
        updateTrackingBot.start();
        Mockito.verify(bot, Mockito.times(1)).setUpdatesListener(Mockito.eq(updateTrackingBot));
    }

    @Test
    public void closeTest() {
        TelegramBot bot = Mockito.mock(TelegramBot.class);
        UpdateTrackingBot updateTrackingBot =
            new UpdateTrackingBot(Mockito.mock(BotMessageProcessor.class), bot);
        updateTrackingBot.start();
        updateTrackingBot.close();
        Mockito.verify(bot, Mockito.times(1)).shutdown();
    }

    @Test
    public void executeNullTest() {
        UpdateTrackingBot updateTrackingBot =  new UpdateTrackingBot(Mockito.mock(BotMessageProcessor.class), null);
        assertThatThrownBy(updateTrackingBot::start).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void executeTest() {
        TelegramBot bot = Mockito.mock(TelegramBot.class);
        UpdateTrackingBot updateTrackingBot =  new UpdateTrackingBot(Mockito.mock(BotMessageProcessor.class), bot);
        SendMessage sendMessage = new SendMessage(1, "text");
        updateTrackingBot.execute(sendMessage);

        Mockito.verify(bot, Mockito.times(1)).execute(sendMessage);
    }
}
