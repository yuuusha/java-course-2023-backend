package edu.java.bot.managers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
public class LinkTest {

    @Test
    public void isURLTrueTest() {
        String link = "https://edu.tinkoff.ru";
        assertTrue(Link.isURL(link));
    }

    @Test
    public void isURLFalseTest() {
        String link = "abcdefg";
        assertFalse(Link.isURL(link));
    }
}
