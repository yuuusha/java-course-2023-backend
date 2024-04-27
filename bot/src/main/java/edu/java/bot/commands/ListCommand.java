package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
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
        ListLinksResponse listLinksResponse = botService.getListLinks(chatId);
        if (listLinksResponse.links().isEmpty()) {
            return new SendMessage(chatId, textProcessor.process("command.list.empty"));
        }
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < listLinksResponse.links().size(); i++) {
            response.append("\n").append(i + 1).append(". ").append(listLinksResponse.links().get(i).url());
        }
        return new SendMessage(chatId, String.format(textProcessor.process("command.list.get"), response));
    }
}
