package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static edu.java.bot.service.URLService.isURL;

@Component
public class TrackCommand extends CommonCommand {

    @Autowired
    public TrackCommand(TextProcessor textProcessor, BotService botService) {
        super(textProcessor, botService);
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
        if (isURL(link)) {
            if (botService.isLinkExist(chatId, link)) {
                return new SendMessage(chatId, textProcessor.process("command.track.exist"));
            }
            botService.addLink(chatId, link);
            return new SendMessage(chatId, textProcessor.process("command.track.success"));
        }
        return new SendMessage(chatId, textProcessor.process("command.track.unsuccess"));
    }
}
