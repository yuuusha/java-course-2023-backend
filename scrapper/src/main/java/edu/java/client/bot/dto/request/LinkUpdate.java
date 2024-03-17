package edu.java.client.bot.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public record LinkUpdate(
    @NotNull Long id,
    @NotNull URL link,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds,

    @NotEmpty Map<String, String> metaInfo
) {
}
