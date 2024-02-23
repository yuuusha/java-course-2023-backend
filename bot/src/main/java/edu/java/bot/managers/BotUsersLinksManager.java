package edu.java.bot.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class BotUsersLinksManager implements UsersLinksManager {

    private final Map<Long, List<Link>> userIdsToLinks = new ConcurrentHashMap<>();


    @Override
    public void registerUser(String userName, Long userId) {
        if (!userIdsToLinks.containsKey(userId)) {
            userIdsToLinks.put(userId, new ArrayList<>());
        }
    }

    @Override
    public boolean isUserRegistered(Long userId) {
        return userIdsToLinks.containsKey(userId);
    }

    @Override
    public boolean linkExist(String link, Long userId) {
        Link newLink = new Link(link);
        return userIdsToLinks.get(userId).contains(newLink);
    }

    @Override
    public void addLink(String link, Long userId) {
        Link newLink = new Link(link);
        if (userIdsToLinks.containsKey(userId) && !userIdsToLinks.get(userId).contains(newLink)) {
            userIdsToLinks.get(userId).add(newLink);
        }
    }

    @Override
    public void removeLink(String link, Long userId) {
        Link curLink = new Link(link);
        if (userIdsToLinks.containsKey(userId)) {
            userIdsToLinks.get(userId).remove(curLink);
        }
    }

    @Override
    public List<Link> getListLinks(Long userId) {
        if (userIdsToLinks.containsKey(userId)) {
            return userIdsToLinks.get(userId);
        }
        return new ArrayList<>();
    }
}
