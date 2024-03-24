package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.ReplacerValiables;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends CommonCommand {

    private final List<Command> commandList;

    @Autowired
    public HelpCommand(TextProcessor textProcessor, BotService botService, List<Command> commandList) {
        super(textProcessor, botService);
        this.commandList = commandList;
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
        StringBuilder responseMessage = new StringBuilder();
        responseMessage.append(textProcessor.process("command.help.util.title"));
        String format = textProcessor.process("command.help.util.help_command_format");
        commandList.forEach(command -> responseMessage.append(
            ReplacerValiables.replaceVariables(format,
                Map.of(
                    "command", command.getCommandName(),
                    "description", command.description()
                )
            )
        ));
        return new SendMessage(update.message().chat().id(), String.valueOf(responseMessage));

    }
}
