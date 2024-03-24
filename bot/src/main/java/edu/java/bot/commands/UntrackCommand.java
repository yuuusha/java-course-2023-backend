package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.dto.OptionalAnswer;
import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;
import edu.java.bot.service.URLService;
import java.util.Map;
import org.springframework.stereotype.Component;

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
        botService.registerUserIfNew(chatId);
        String[] elements = update.message().text().split(" ");

        if (isEmptyArgument(elements)) {
            return new SendMessage(
                chatId,
                textProcessor.process("message.empty_argument")
            );
        }

        LinkResponse link;
        StringBuilder answerString = new StringBuilder();
        for (int i = 1; i < elements.length; i++) {
            if (!elements[i].isEmpty()) {
                answerString.append(getAnswerForLink(elements[i], chatId)).append('\n');
            }
        }
        answerString.deleteCharAt(answerString.length() - 1);

        return new SendMessage(chatId, answerString.toString());
    }

    private String getAnswerForLink(String link, Long chatId) {
        if (!URLService.isURL(link)) {
            if (!link.isEmpty()) {
                return textProcessor.process("message.invalid_argument", Map.of("argument", link));
            }
        }

        OptionalAnswer<LinkResponse> answer = botService.unTrackUserLink(chatId, link);
        if (answer != null) {
            if (!answer.isError()) {
                return textProcessor.process("command.untrack.messages.successful_untrack", Map.of("link", link));
            } else {
                return answer.apiErrorResponse().description();
            }
        } else {
            return textProcessor.process("message.unknown_command");
        }
    }
}
