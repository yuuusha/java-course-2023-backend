package edu.java.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.service.LinkUpdatesSenderService;
import java.net.URI;
import java.util.List;
import java.util.Map;
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

@WebMvcTest(LinkUpdatesController.class)
public class LinkUpdatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkUpdatesSenderService linkUpdatesSenderService;

    @SneakyThrows @Test
    public void sendLinkUpdateCorrectTest() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/updates")
                .content("""
                    {
                      "id": 42,
                      "url": "https://google.com",
                      "description": "string",
                      "tgChatIds": [
                        0
                      ],
                      "metaInfo": {}
                    }
                    """)
                .contentType("application/json")
        ).andExpect(status().isOk());

        Mockito.verify(linkUpdatesSenderService).sendLinkUpdate(
            new LinkUpdate(42L, URI.create("https://google.com").toURL(), "string", List.of(0L), Map.of())
        );
    }

    @Test
    @SneakyThrows
    public void sendLinkUpdateInCorrectTest() {
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/updates")
                .content("""
                    {
                      "id": 42,
                      "url": "https://google.com",
                      "description": "string"
                    }
                    """)
                .contentType("application/json")
        ).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "org.springframework.web.bind.MethodArgumentNotValidException");
        Mockito.verify(linkUpdatesSenderService, Mockito.times(0)).sendLinkUpdate(Mockito.any());
    }
}
