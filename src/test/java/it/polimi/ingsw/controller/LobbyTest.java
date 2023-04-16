package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;

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