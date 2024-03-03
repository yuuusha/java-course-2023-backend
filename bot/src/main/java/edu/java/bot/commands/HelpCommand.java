package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends CommonCommand {

    @Autowired
    public HelpCommand(TextProcessor textProcessor, BotService botService) {
        super(textProcessor, botService);
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
