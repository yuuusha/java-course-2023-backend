package edu.java.bot.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class LinkTest {

    @Test
    public void isURLTrueTest() {
        String link = "https://edu.tinkoff.ru";
        Assertions.assertTrue(Link.isURL(link));
    }

    @Test
    public void isURLFalseTest() {
        String link = "abcdefg";
        Assertions.assertFalse(Link.isURL(link));
    }
}
