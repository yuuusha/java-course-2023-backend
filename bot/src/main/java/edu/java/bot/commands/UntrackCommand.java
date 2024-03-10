package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import org.springframework.stereotype.Component;
import static edu.java.bot.service.URLService.isURL;

@Component
public class UntrackCommand extends CommonCommand {

    public UntrackCommand(TextProcessor textProcessor, BotService botService) {
        super(textProcessor, botService);
    }

    @Override
    public String getCommandName() {
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
        if (isURL(link)) {
            if (botService.isLinkExist(chatId, link)) {
                botService.removeLink(chatId, link);
                return new SendMessage(chatId, textProcessor.process("command.untrack.success"));
            }
        }
        return new SendMessage(chatId, textProcessor.process("command.untrack.unsuccess"));
    }
}
