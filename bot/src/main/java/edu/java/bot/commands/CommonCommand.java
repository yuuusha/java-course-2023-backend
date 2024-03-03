package edu.java.bot.commands;

import edu.java.bot.processors.TextProcessor;
import edu.java.bot.service.BotService;

public abstract class CommonCommand implements Command {

    protected TextProcessor textProcessor;
    protected BotService botService;

    public CommonCommand(TextProcessor textProcessor, BotService linksManager) {
        this.botService = linksManager;
        this.textProcessor = textProcessor;
    }
}
