package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controller.TelegramChatController;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.service.TelegramChatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TelegramChatController.class)
public class TelegramChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TelegramChatService chatService;

    @Test
    public void registerChatCorrectTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/tg-chat/42")
        ).andExpect(status().isOk());

        Mockito.verify(chatService).registerChat(42L);
    }

    @Test
    public void registerChatAlreadyRegisteredTest() throws Exception {
        Mockito.doThrow(new ChatAlreadyRegisteredException(42L)).when(chatService).registerChat(42L);
        var result = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/tg-chat/42")
        ).andExpect(status().isBadRequest()).andReturn();

        ApiErrorResponse error =
            objectMapper.readValue(result.getResponse().getContentAsString(), ApiErrorResponse.class);
        Assertions.assertThat(error).extracting("code", "exceptionName")
            .contains("400", "ChatAlreadyRegisteredException");

        Mockito.verify(chatService).registerChat(42L);
    }

    @Test
    public void deleteChatCorrectTest() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/tg-chat/42")
        ).andExpect(status().isOk());

        Mockito.verify(chatService).deleteChat(42L);
    }

    @Test
    public void deleteChatIncorrectTest() throws Exception {
        Mockito.doThrow(new ChatNotFoundException(42L)).when(chatService).deleteChat(42L);
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/tg-chat/42")
        ).andExpect(status().isNotFound());

        Mockito.verify(chatService).deleteChat(42L);
    }

}
