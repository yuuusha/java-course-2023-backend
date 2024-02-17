package edu.java.bot.managers;

import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BotUsersLinksManagerTest {

    private BotUsersLinksManager manager;

    @Before
    public void setUp() {
        manager = new BotUsersLinksManager();
    }

    @Test
    public void isUserRegisteredTrueTest() {
        manager.registerUser("username", 42L);
        assertTrue(manager.isUserRegistered(42L));
    }

    @Test
    public void isUserRegisteredFalseTest() {
        assertFalse(manager.isUserRegistered(42L));
    }

    @Test
    public void addLinkTest() {
        manager.registerUser("username", 42L);
        manager.addLink("google.com", 42L);
        assertTrue(manager.linkExist("google.com", 42L));
    }

    @Test
    public void linkExistFalseTest() {
        manager.registerUser("username", 42L);
        assertFalse(manager.linkExist("google.com", 42L));
    }

    @Test
    public void removeLinkTest() {
        manager.registerUser("username", 42L);
        manager.addLink("google.com", 42L);
        manager.removeLink("google.com", 42L);
        assertFalse(manager.linkExist("google.com", 42L));
    }

    @Test
    public void getListLinksTest() {
        manager.registerUser("username", 42L);
        manager.addLink("google.com", 42L);
        assertThat(manager.getListLinks(42L)).hasSize(1).element(0).isEqualTo(new Link("google.com"));
    }
}

