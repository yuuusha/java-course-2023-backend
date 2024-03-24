package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends CommonCommand {

    @Autowired
    public ListCommand(TextProcessor textProcessor, BotService botService) {
        super(textProcessor, botService);
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
        if (botService.userLinks(chatId).answer().links().isEmpty()) {
            return new SendMessage(chatId, textProcessor.process("command.list.messages.empty_list_of_links"));
        }
        StringBuilder linksString = new StringBuilder();
        linksString.append(textProcessor.process("command.list.messages.show_tracked_links"));
        int id = 1;
        for (LinkResponse link : botService.userLinks(chatId).answer().links()) {
            linksString.append(id).append(". ").append(link.url()).append("\n");
            id++;
        }
        return new SendMessage(chatId, String.valueOf(linksString));
    }
}
