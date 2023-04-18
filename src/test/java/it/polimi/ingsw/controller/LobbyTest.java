package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LobbyTest {


    @Test
    void addFirstPlayer() {
        Lobby lobby = new Lobby("test");
        assertFalse(lobby.addFirstPlayer("p1", 5));
        assertTrue(lobby.addFirstPlayer("p1", 4));
        assertFalse(lobby.addFirstPlayer("p2", 3));

        lobby = new Lobby("test");
        assertFalse(lobby.addFirstPlayer("p1", 1));
        assertTrue(lobby.addFirstPlayer("p1", 2));
    }

    @Test
    void addPlayer() {
        Lobby lobby = new Lobby("test");
        assertTrue(lobby.addFirstPlayer("p1", 4));
        try {
            assertTrue(lobby.addPlayer("p2"));
        }
        catch (Exception e){
            fail();
        }
        assertThrows(NicknameTakenException.class, lobby.addPlayer("p2"));
        assertThrows(NicknameTakenException.class, lobby.addPlayer("p1"));
        assertTrue(lobby.addPlayer("p3"));
        assertTrue(lobby.addPlayer("p4"));
        assertThrows(FullGameException.class, lobby.addPlayer("p5"));
    }

    @Test
    void removePlayer() {
    }

    @Test
    void setEasyRules() {
    }

    @Test
    void isReadyToPlay() {
    }

    @Test
    void startGame() {
    }

    @Test
    void getSavedGames() {
    }

    @Test
    void loadGame() {
    }
}