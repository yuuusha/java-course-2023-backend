package edu.java.bot.commands;

import edu.java.bot.managers.UsersLinksManager;
import edu.java.bot.processors.TextProcessor;

public abstract class CommonCommand implements Command {

    protected TextProcessor textProcessor;
    protected UsersLinksManager linksManager;

    public CommonCommand(TextProcessor textProcessor, UsersLinksManager linksManager) {
        this.linksManager = linksManager;
        this.textProcessor = textProcessor;
    }
}
