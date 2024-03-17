package edu.java.repository;

import edu.java.dto.Chat;
import java.util.List;

public interface ChatRepository {

    List<Chat> findAll();

    void add(long chatId);

    void remove(long chatId);

    boolean isExists(long chatId);
}
