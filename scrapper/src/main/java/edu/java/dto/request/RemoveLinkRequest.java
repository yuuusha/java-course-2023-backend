package edu.java.dto.request;

import java.net.URL;
import org.jetbrains.annotations.NotNull;

public record RemoveLinkRequest(
    @NotNull URL url
) {
}
