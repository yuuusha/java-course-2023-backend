package edu.java.bot.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BotMessageProcessor implements MessageProcessor {

    private final List<Command> commands;

    private final TextProcessor textProcessor;

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
        if (update.message() != null && update.message().text() != null) {
            SendMessage result =
                new SendMessage(update.message().chat().id(), textProcessor.process("message.unknown_command"));
            for (Command command : commands()) {
                String userCommand = update.message().text().trim();
                String botCommand = command.getCommandName();
                String firstWordOfUserCommand = userCommand.split("\\s+")[0];
                if (firstWordOfUserCommand.equals(botCommand)) {
                    result = command.handle(update);
                }
            }
            return result;
        }
        return null;
    }
}
