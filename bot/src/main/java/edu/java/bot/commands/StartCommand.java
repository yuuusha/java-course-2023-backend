package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends CommonCommand {

    @Autowired
    public StartCommand(TextProcessor textProcessor, UsersLinksManager linksManager) {
        super(textProcessor, linksManager);
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
        String userName = update.message().chat().firstName();
        linksManager.registerUser(userName, chatId);
        return new SendMessage(chatId, textProcessor.process("command.start.message"));
    }
}

