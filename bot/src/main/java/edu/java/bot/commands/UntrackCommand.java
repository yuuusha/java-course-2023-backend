package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.Link;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand extends CommonCommand {

    public UntrackCommand(TextProcessor textProcessor, UsersLinksManager linksManager) {
        super(textProcessor, linksManager);
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return textProcessor.process("command.untrack.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String userRequest = update.message().text();
        String link = userRequest.replace("/untrack ", "");
        if (Link.isURL(link)) {
            if (linksManager.linkExist(link, chatId)) {
                linksManager.removeLink(link, chatId);
                return new SendMessage(chatId, textProcessor.process("command.untrack.success"));
            }
        }
        return new SendMessage(chatId, textProcessor.process("command.untrack.unsuccess"));
    }
}
