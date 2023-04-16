package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayControllerTest {

    @Test
    void saveGame() throws IOException, URISyntaxException {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "saves");
        assertTrue(playController.saveGame());
    }

    @Test
    void getPlayerPoints() {
    }
}