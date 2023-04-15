package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.PlayController;
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
        List<Player> players = new ArrayList<>();
        players.add(new Player("player1"));
        players.add(new Player("player2"));
        players.add(new Player("player3"));
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "saves");
        assertTrue(playController.saveGame());
    }

    @Test
    void getPlayerPoints() {
    }
}