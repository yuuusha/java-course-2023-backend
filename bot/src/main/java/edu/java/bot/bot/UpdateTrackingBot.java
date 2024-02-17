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
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateTrackingBot implements Bot {

    private final TelegramBot telegramBot;
    private final MessageProcessor userMessagesProcessor;

    @Autowired
    public UpdateTrackingBot(MessageProcessor userMessagesProcessor, TelegramBot telegramBot) {
        this.userMessagesProcessor = userMessagesProcessor;
        this.telegramBot = telegramBot;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        if (telegramBot == null) {
            throw new IllegalStateException("Telegram bot is not working");
        }
        if (request != null) {
            telegramBot.execute(request);
        }
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            SendMessage sendMessage = userMessagesProcessor.process(update);
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

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
