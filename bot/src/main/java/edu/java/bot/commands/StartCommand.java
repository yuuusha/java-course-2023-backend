package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends CommonCommand {

    @Autowired
    public StartCommand(TextProcessor textProcessor, BotService botService) {
        super(textProcessor, botService);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public String description() {
        return textProcessor.process("command.start.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        botService.registerUser(chatId);
        return new SendMessage(chatId, textProcessor.process("command.start.message"));
    }
}

