package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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

    @Test
    void leave() {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "saves");
        assertTrue(playController.leave("player1"));
        assertEquals(1, game.getPlayersList().stream().filter(p -> !p.isPlaying()).count());
        assertFalse(playController.leave("player1"));
        assertEquals(1, game.getPlayersList().stream().filter(p -> !p.isPlaying()).count());
        assertFalse(playController.leave("player4"));
    }

    @Test
    void reconnect(){
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "saves");
        assertFalse(playController.reconnect("player1"));
        assertTrue(playController.leave("player1"));
        assertTrue(playController.reconnect("player1"));
        assertEquals(0, game.getPlayersList().stream().filter(p -> !p.isPlaying()).count());
        assertFalse(playController.reconnect("player4"));
    }
}