package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends CommonCommand {

    @Autowired
    public HelpCommand(TextProcessor textProcessor, UsersLinksManager linksManager) {
        super(textProcessor, linksManager);
    }

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public String description() {
        return textProcessor.process("command.help.description");
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), textProcessor.process("command.help.show"));
    }
}
