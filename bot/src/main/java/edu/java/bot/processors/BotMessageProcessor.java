package edu.java.bot.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotMessageProcessor implements MessageProcessor {

    private final List<Command> commands;

    private final TextProcessor textProcessor;

    @Autowired
    public BotMessageProcessor(List<Command> commands, TextProcessor textProcessor) {
        this.commands = commands;
        this.textProcessor = textProcessor;
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        if (update.message() == null || update.message().text() == null) {
            return null;
        }
        for (Command command : commands()) {
            if (update.message() != null && update.message().text() != null) {
                String userCommand = update.message().text().trim();
                String botCommand = command.command();
                String firstWordOfUserCommand = userCommand.split("\\s+")[0];
                if (firstWordOfUserCommand.equals(botCommand)) {
                    return command.handle(update);
                }
            }
        }
        return new SendMessage(update.message().chat().id(), textProcessor.process("message.unknown_command"));
    }
}
