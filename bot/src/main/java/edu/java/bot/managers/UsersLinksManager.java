package edu.java.bot.managers;

import java.util.List;

public interface UsersLinksManager {

    void registerUser(String userName, Long userId);

    boolean isUserRegistered(Long userId);

    boolean linkExist(String link, Long userId);

    void addLink(String link, Long userId);

    void removeLink(String link, Long userId);

    List<Link> getListLinks(Long userId);

}
