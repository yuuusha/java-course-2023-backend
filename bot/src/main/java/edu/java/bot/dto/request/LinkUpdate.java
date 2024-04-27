package edu.java.bot.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.net.URL;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LinkUpdate(
    @NotNull Long id,
    @NotNull URL url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) {
}
