package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.managers.Link;
import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand extends CommonCommand {

    @Autowired
    public TrackCommand(TextProcessor textProcessor, UsersLinksManager linksManager) {
        super(textProcessor, linksManager);
    }

    @Override
    public String getCommandName() {
        return "/track";
    }

    @Override
    public String description() {
        return textProcessor.process("command.track.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String userRequest = update.message().text();
        String link = userRequest.replace("/track ", "");
        if (Link.isURL(link)) {
            if (linksManager.linkExist(link, chatId)) {
                return new SendMessage(chatId, textProcessor.process("command.track.exist"));
            }
            linksManager.addLink(link, chatId);
            return new SendMessage(chatId, textProcessor.process("command.track.success"));
        }
        return new SendMessage(chatId, textProcessor.process("command.track.unsuccess"));
    }
}
