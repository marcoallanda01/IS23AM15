package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {


    @Test
    void addFirstPlayer() {
    }

    @Test
    void addPlayer() {
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
        Lobby lobby = new Lobby("saves");
        Set<String> savedGames = lobby.getSavedGames();
        File saves = new File("saves");
        File[] savesList = saves.listFiles();
        int n = 0;
        if (savesList != null) {
            n = savesList.length;
        }
        //System.out.println(savedGames);
        assertEquals(savedGames.size(), n);

    }

    @Test
    void loadGame() throws GameLoadException, GameNameException, IOException, IllegalLobbyException, WaitLobbyException {
        Lobby lobby = new Lobby("saves");
        Optional<String> uniqueID = lobby.join();
        if(uniqueID.isEmpty()){
            fail();
        }
        File saves = new File("saves");
        if (!saves.exists()) {
            throw new IOException("Can not find " + saves);
        }
        File[] savesList = saves.listFiles();
        int n = 0;
        if (savesList != null) {
            n = savesList.length;
        }
        List<String> playersLoaded =  lobby.loadGame(String.valueOf(n-1), uniqueID.get());
        assertNotEquals(0, playersLoaded.size());

    }
}