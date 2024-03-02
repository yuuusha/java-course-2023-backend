package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;

public class Utils {

    public static Update createMockUpdate() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(update.message()).thenReturn(message);
        return update;
    }

    public static Update createMockUpdate(String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.text()).thenReturn(text);
        Mockito.when(update.message()).thenReturn(message);
        return update;
    }

    public static Update createMockUpdate(String text, Long chatId) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn(text);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.chat().id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
        return update;
    }

    public static ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSourceResourceBundle = new ResourceBundleMessageSource();
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("message.yml"));
        messageSourceResourceBundle.setCommonMessages(yamlPropertiesFactoryBean.getObject());
        return messageSourceResourceBundle;
    }
}
