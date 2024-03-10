package edu.java.controller;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/links", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping
    public ListLinksResponse getTrackedLinks(@RequestHeader(name = "Tg-Chat-Id") @Valid Long chatId) {
        return linkService.getTrackedLinks(chatId);
    }

    @PostMapping
    public LinkResponse addTrackingLink(
        @RequestHeader(name = "Tg-Chat-Id") Long chatId,
        @Valid @RequestBody AddLinkRequest request
    ) {
        return linkService.addTrackingLink(request.url(), chatId);
    }

    @DeleteMapping
    public LinkResponse deleteTrackingLink(
        @RequestHeader(name = "Tg-Chat-Id") Long chatId,
        @Valid @RequestBody RemoveLinkRequest request
    ) {
        return linkService.deleteTrackingLink(request.url(), chatId);
    }
}
