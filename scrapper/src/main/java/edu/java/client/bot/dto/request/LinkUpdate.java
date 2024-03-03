package edu.java.client.bot.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LinkUpdate(
    @NotNull Long id,
    @NotNull URI link,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) {
}
