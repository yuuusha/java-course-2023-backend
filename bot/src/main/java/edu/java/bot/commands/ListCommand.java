package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.Link;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends CommonCommand {

    @Autowired
    public ListCommand(TextProcessor textProcessor, UsersLinksManager linksManager) {
        super(textProcessor, linksManager);
    }

    @Override
    public String getCommandName() {
        return "/list";
    }

    @Override
    public String description() {
        return textProcessor.process("command.list.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        List<Link> linkList = linksManager.getListLinks(chatId);
        if (linkList.isEmpty()) {
            return new SendMessage(chatId, textProcessor.process("command.list.empty"));
        }
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < linkList.size(); i++) {
            response.append("\n").append(i + 1).append(". ").append(linkList.get(i).url());
        }
        return new SendMessage(chatId, String.format(textProcessor.process("command.list.get"), response));
    }
}
