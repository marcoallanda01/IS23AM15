package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getBookShelf() {
        Player player = new Player("test");
        assertEquals(6, player.getBookShelf().getMaxColumnSpace());
    }

    @Test
    void getUserName() {
        Player player = new Player("test");
        assertEquals("test", player.getUserName());
    }

    @Test
    void isPlaying() {
        Player player = new Player("test");
        assertTrue(player.isPlaying());
        player.setPlaying(false);
        assertFalse(player.isPlaying());
    }

    @Test
    void isFirstToFinish() {
        Player player = new Player("test");
        assertFalse(player.isFirstToFinish());
        player.setFirstToFinish(true);
        assertTrue(player.isFirstToFinish());
    }

    @Test
    void hasFinished() {
        Player player = new Player("test");
        assertFalse(player.hasFinished());
        player.setFullBookShelf(true);
        assertTrue(player.hasFinished());
    }
}