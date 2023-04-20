package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        it.polimi.ingsw.server.controller.Lobby lobby = new it.polimi.ingsw.server.controller.Lobby("saves");
        Set<String> savedGames = lobby.getSavedGames();
        System.out.println("savedGames = " + savedGames);
    }

    @Test
    void loadGame() throws it.polimi.ingsw.server.controller.GameLoadException, it.polimi.ingsw.server.controller.GameNameException, IOException, it.polimi.ingsw.server.controller.IllegalLobbyException, it.polimi.ingsw.server.controller.WaitLobbyException {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        it.polimi.ingsw.server.controller.PlayController playController = new it.polimi.ingsw.server.controller.PlayController(game, "saves");
        assertTrue(playController.saveGame());

        it.polimi.ingsw.server.controller.Lobby lobby = new it.polimi.ingsw.server.controller.Lobby("saves");
        String uniqueID = String.valueOf(lobby.join());
        File saves = new File("saves");
        if (!saves.exists()) {
            throw new IOException("Can not find " + saves);
        }
        File[] savesList = saves.listFiles();
        int n = 0;
        if (savesList != null) {
            n = savesList.length;
        }
        List<String> playersLoaded =  lobby.loadGame(String.valueOf(n-1), uniqueID);
        assertEquals(players, playersLoaded);

    }
}