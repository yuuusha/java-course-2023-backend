package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controller.LinkController;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkIsNotSupportedException;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
public class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkService linkService;

    @SneakyThrows @Test
    public void getTrackedLinksCorrectTest() {
        Mockito.when(linkService.getTrackedLinks(42L))
            .thenReturn(new ListLinksResponse(List.of(new LinkResponse(42L, URI.create("https://google.com").toURL())), 1));

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/links")
                .header("Tg-Chat-Id", 42L)
                .contentType("application/json")
        ).andExpect(status().isOk()).andExpect(
            result -> Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo("{\"links\":[{\"id\":42,\"url\":\"https://google.com\"}],\"size\":1}")
        );
        Mockito.verify(linkService).getTrackedLinks(42L);
    }

    @SneakyThrows @Test
    public void getTrackedLinksMissingChatTest() {
        Mockito.when(linkService.getTrackedLinks(42L))
            .thenThrow(new ChatNotFoundException(42L));
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/links")
                .contentType("application/json")
                .header("Tg-Chat-Id", 42L)
        ).andExpect(status().isNotFound()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("404", "ChatNotFoundException");
    }

    @SneakyThrows @Test
    public void getTrackedLinksMissingHeaderTgChatIdTest() {
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/links")
                .contentType("application/json")
        ).andExpect(status().isBadRequest()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "org.springframework.web.bind.MissingRequestHeaderException");
    }

    @Test
    public void addTrackingLinkCorrectTest() throws Exception {
        Mockito.when(linkService.addTrackingLink(URI.create("https://google.com").toURL(), 42L))
            .thenReturn(new LinkResponse(42L, URI.create("https://google.com").toURL()));
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/links")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddLinkRequest(URI.create("https://google.com").toURL())))
                .header("Tg-Chat-Id", 42L)
        ).andExpect(status().isOk()).andExpect(
            result -> Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo("{\"id\":42,\"url\":\"https://google.com\"}")
        );

        Mockito.verify(linkService).addTrackingLink(URI.create("https://google.com").toURL(), 42L);
    }

    @Test
    public void addTrackingLinkIncorrectLinkTest() throws Exception {
        Mockito.when(linkService.addTrackingLink(URI.create("https://google2.com").toURL(), 42L))
            .thenThrow(new LinkIsNotSupportedException(URI.create("https://google2.com").toURL()));
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/links")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddLinkRequest(URI.create("https://google2.com").toURL())))
                .header("Tg-Chat-Id", 42L)
        ).andExpect(status().isBadRequest()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "LinkIsNotSupportedException");
    }

    @Test
    public void deleteTrackingLinkCorrectTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/links")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(URI.create("https://google.com").toURL())))
                .header("Tg-Chat-Id", 42L)
        ).andExpect(status().isOk());

        Mockito.verify(linkService).deleteTrackingLink(URI.create("https://google.com").toURL(), 42L);
    }
}
