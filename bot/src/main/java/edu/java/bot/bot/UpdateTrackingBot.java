package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.processors.MessageProcessor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateTrackingBot implements Bot {

    private final TelegramBot telegramBot;
    private final MessageProcessor userMessagesProcessor;
    private final MeterRegistry meterRegistry;
    private Counter userMessagesCounter;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            SendMessage sendMessage = userMessagesProcessor.process(update);
            userMessagesCounter.increment();
            execute(sendMessage);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        execute(new SetMyCommands((userMessagesProcessor.commands().stream().map(Command::toApiCommand).toList()
            .toArray(new BotCommand[0]))));
        telegramBot.setUpdatesListener(this);
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        if (telegramBot == null) {
            throw new IllegalStateException("Telegram bot is not working");
        }
        telegramBot.execute(request);
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }

    @PostConstruct
    public void initMetrics() {
        userMessagesCounter = Counter.builder("user_messages")
            .description("Count of processed user messages")
            .register(meterRegistry);
    }
}
