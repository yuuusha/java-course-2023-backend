package edu.java.dto.response;

import edu.java.dto.Chat;
import java.util.List;

public record ListChatsResponse(
    List<Chat> chats,
    Integer size
) {
}
