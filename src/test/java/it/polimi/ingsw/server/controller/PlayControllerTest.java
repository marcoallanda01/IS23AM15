package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayControllerTest {

    @Test
    void saveGame() throws IOException, SaveException {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "saves");
        assertTrue(playController.saveGame("SaveGameTest", true));
    }

    @Test
    void saveGameExceptions() throws SaveException, IOException {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "/");
        assertThrows(IOException.class, () -> playController.saveGame("SaveGameTest"));
    }

    @Test
    void getPlayerPoints() {
    }
}