package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.server.model.turn.PutTilesState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void pickTiles() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> players = List.of(p1.getUserName(), p2.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(players,false);
        game.getBoard().fillBoard();
        List<Tile> tiles = List.of(game.getBoard().getBoard().get(1).get(3), game.getBoard().getBoard().get(1).get(4));
        assertTrue(game.pickTiles(tiles));
        assertEquals(tiles, game.getCurrentTurn().getPickedTiles());
        List<Tile> retryTiles = List.of(game.getBoard().getBoard().get(3).get(3), game.getBoard().getBoard().get(3).get(4));
        assertFalse(game.pickTiles(retryTiles));
    }

    @Test
    void putTiles() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> players = List.of(p1.getUserName(), p2.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(players,false);
        game.getBoard().fillBoard();
        Player player = game.getCurrentTurn().getCurrentPlayer();
        List<Tile> tiles = List.of(game.getBoard().getBoard().get(1).get(3), game.getBoard().getBoard().get(1).get(4));
        assertTrue(game.pickTiles(tiles));
        game.getCurrentTurn().changeState(new PutTilesState(game.getCurrentTurn()));
        assertTrue(game.putTiles(tiles, 0));
        assertEquals(tiles, List.of(player.getBookShelf().getTile(0,0), player.getBookShelf().getTile(0,1)));
    }

    @Test
    void sendMessage() throws PlayerNotFoundException {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        List<String> playersName = List.of(p1.getUserName(), p2.getUserName(), p3.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(playersName,false);
        game.sendMessage(p1.getUserName(), p2.getUserName(), "ciao p2");
        game.sendMessage(p2.getUserName(), p1.getUserName(), "ciao p1");
        game.sendMessage(p3.getUserName(), p1.getUserName(), "ciao p1");
        assertThrows(PlayerNotFoundException.class, ()->game.sendMessage(p3.getUserName(), "nonPresente", "ciao p1"));
        assertThrows(PlayerNotFoundException.class, ()->game.sendMessage("nonPresente",  p1.getUserName(), "ciao p1"));
        game.sendMessage(p1.getUserName(), "ciao a tutti");
        assertEquals(game.getChat().getMessages(p1).size(), 3);
        assertEquals(game.getChat().getMessages(p2).size(), 2);
        assertEquals(game.getChat().getMessages(p3).size(), 1);
        assertThrows(PlayerNotFoundException.class, () -> game.sendMessage(p1.getUserName(), "p5", "ciao p5"));
        assertThrows(PlayerNotFoundException.class, () -> game.sendMessage("p5", p1.getUserName(), "ciao p5"));

    }

    @Test
    void disconnectPlayer() throws PlayerNotFoundException {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> playersName = List.of(p1.getUserName(), p2.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(playersName,false);
        assertTrue(game.disconnectPlayer(p1.getUserName()));
        assertFalse(game.getPlayerFromNickname(p1.getUserName()).isPlaying());
        assertTrue(game.getPlayerFromNickname(p2.getUserName()).isPlaying());
    }

    @Test
    void reconnectPlayer() throws PlayerNotFoundException {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> playersName = List.of(p1.getUserName(), p2.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(playersName,false);
        assertTrue(game.disconnectPlayer(p1.getUserName()));
        assertFalse(game.getPlayerFromNickname(p1.getUserName()).isPlaying());
        assertTrue(game.getPlayerFromNickname(p2.getUserName()).isPlaying());
        assertTrue(game.reconnectPlayer(p1.getUserName()));
        assertTrue(game.getPlayerFromNickname(p1.getUserName()).isPlaying());
        assertTrue(game.getPlayerFromNickname(p2.getUserName()).isPlaying());
    }
}