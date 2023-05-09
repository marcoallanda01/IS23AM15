package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.communication.ServerCommunication;
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
        Lobby lobby = new Lobby("/src/main/resources/saves");
        Set<String> savedGames = lobby.getSavedGames();
        File saves = new File("/src/main/resources/saves");
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
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        Lobby lobby = new Lobby("saves");
        Optional<String> uniqueID = lobby.join();
        if(uniqueID.isEmpty()){
            fail();
        }
        File saves = new File("saves");
        if (!saves.exists()) {
            System.err.println(saves.getAbsolutePath());
            throw new IOException("Can not find " + saves);
        }
        List<String> playersLoaded =  lobby.loadGame("SaveGameTest", uniqueID.get());
        assertNotEquals(0, playersLoaded.size());
    }
}