package edu.java.bot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static edu.java.bot.service.URLService.isURL;

public class URLServiceTest {

    @Test
    public void isURLTrueTest() {
        String link = "https://edu.tinkoff.ru";
        Assertions.assertTrue(isURL(link));
    }

    @Test
    public void isURLFalseTest() {
        String link = "abcdefg";
        Assertions.assertFalse(isURL(link));
    }

}
