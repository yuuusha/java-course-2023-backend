package edu.java.bot.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class BotUsersLinksManager implements UsersLinksManager {

    private final Map<Long, List<Link>> usersLinks = new ConcurrentHashMap<>();

    @Override
    public void registerUser(String userName, Long userId) {
        if (!usersLinks.containsKey(userId)) {
            usersLinks.put(userId, new ArrayList<>());
        }
    }

    @Override
    public boolean isUserRegistered(Long userId) {
        return usersLinks.containsKey(userId);
    }

    @Override
    public boolean linkExist(String link, Long userId) {
        Link newLink = new Link(link);
        return usersLinks.get(userId).contains(newLink);
    }

    @Override
    public void addLink(String link, Long userId) {
        Link newLink = new Link(link);
        if (usersLinks.containsKey(userId) && !usersLinks.get(userId).contains(newLink)) {
            usersLinks.get(userId).add(newLink);
        }
    }

    @Override
    public void removeLink(String link, Long userId) {
        Link curLink = new Link(link);
        if (usersLinks.containsKey(userId)) {
            usersLinks.get(userId).remove(curLink);
        }
    }

    @Override
    public List<Link> getListLinks(Long userId) {
        if (usersLinks.containsKey(userId)) {
            return usersLinks.get(userId);
        }
        return new ArrayList<>();
    }
}
