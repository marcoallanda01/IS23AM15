package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.model.exceptions.ArrestGameException;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.server.model.turn.PutTilesState;
import it.polimi.ingsw.server.model.turn.Turn;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void pickTiles() {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> players = List.of(p1.getUserName(), p2.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(players,false, null);
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
        game.setGame(players,false,null);
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
        game.setGame(playersName,false,null);
        game.sendMessage(p1.getUserName(), p2.getUserName(), "ciao p2");
        game.sendMessage(p2.getUserName(), p1.getUserName(), "ciao p1");
        game.sendMessage(p3.getUserName(), p1.getUserName(), "ciao p1");
        assertThrows(PlayerNotFoundException.class, ()->game.sendMessage(p3.getUserName(), "nonPresente", "ciao p1"));
        assertThrows(PlayerNotFoundException.class, ()->game.sendMessage("nonPresente",  p1.getUserName(), "ciao p1"));
        game.sendMessage(p1.getUserName(), "ciao a tutti");
        assertEquals(game.getChat().getMessages(p1).size(), 4);
        assertEquals(game.getChat().getMessages(p2).size(), 3);
        assertEquals(game.getChat().getMessages(p3).size(), 2);
        assertThrows(PlayerNotFoundException.class, () -> game.sendMessage(p1.getUserName(), "p5", "ciao p5"));
        assertThrows(PlayerNotFoundException.class, () -> game.sendMessage("p5", p1.getUserName(), "ciao p5"));

    }

    @Test
    void disconnectPlayer() throws PlayerNotFoundException {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> playersName = List.of(p1.getUserName(), p2.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(playersName,false, null);
        assertTrue(game.disconnectPlayer(p1.getUserName()));
        assertFalse(game.getPlayerFromNickname(p1.getUserName()).isPlaying());
        assertTrue(game.getPlayerFromNickname(p2.getUserName()).isPlaying());
    }

    @Test
    void nextTurnPlayerDisconnected(){
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        List<String> playersName = List.of(p1.getUserName(), p2.getUserName(), p3.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(playersName,false, null);
        game.disconnectPlayer(game.getPlayers().get(1));
        game.disconnectPlayer(game.getPlayers().get(0));
        assertEquals(game.getPlayers().get(2), game.getCurrentPlayer());
        game.reconnectPlayer(game.getPlayers().get(1));
        assertEquals(game.getPlayers().get(2), game.getCurrentPlayer());
    }

    @Test
    void reconnectPlayer() throws PlayerNotFoundException {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> playersName = List.of(p1.getUserName(), p2.getUserName());
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(playersName,false, null);
        assertTrue(game.disconnectPlayer(p1.getUserName()));
        assertFalse(game.getPlayerFromNickname(p1.getUserName()).isPlaying());
        assertTrue(game.getPlayerFromNickname(p2.getUserName()).isPlaying());
        assertTrue(game.reconnectPlayer(p1.getUserName()));
        assertTrue(game.getPlayerFromNickname(p1.getUserName()).isPlaying());
        assertTrue(game.getPlayerFromNickname(p2.getUserName()).isPlaying());
    }

    @Test
    void automaticBoardRefill(){
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(List.of("p1", "p2"), false, null);


        Map<Integer, Map<Integer, TileRule>> mask = new HashMap<>();
        mask.put(0, Map.of(0, TileRule.BLOCK, 1, TileRule.TWO, 2, TileRule.BLOCK,3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(1, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(2, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(3, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(4,
                Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8, TileRule.BLOCK));
        mask.put(5,
                Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8, TileRule.BLOCK));
        mask.put(6, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(7, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(8, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        LivingRoomBoard board = new LivingRoomBoard(2, mask);
        board.fillBoard();

        game.setGame(new Game(game.getPlayersList(),null, false, board, new Turn(game.getPlayersList().get(0), board),game.getChat(), game.getGoalManager()));

        List<Tile> tiles = board.getAllTiles();
        assertEquals(1, board.getAllTiles().size());
        assertTrue(game.pickTiles(tiles));
        assertEquals(1, board.getAllTiles().size());
    }

    @Test
    void disconnectAfterPick(){
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        game.setGame(List.of("p1", "p2"), false, null);


        Map<Integer, Map<Integer, TileRule>> mask = new HashMap<>();
        mask.put(0, Map.of(0, TileRule.BLOCK, 1, TileRule.TWO, 2, TileRule.BLOCK,3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(1, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(2, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(3, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(4,
                Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8, TileRule.BLOCK));
        mask.put(5,
                Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8, TileRule.BLOCK));
        mask.put(6, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(7, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(8, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        LivingRoomBoard board = new LivingRoomBoard(2, mask);
        board.fillBoard();

        game.setGame(new Game(game.getPlayersList(),null, false, board, new Turn(game.getPlayersList().get(0), board),game.getChat(), game.getGoalManager()));

        while(true){
            board.removeFromBoard(board.getAllTiles().stream().toList());
            try{
                board.fillBoard();
            }catch (ArrestGameException e){
                break;
            }
        }
        assertThrows(ArrestGameException.class, board::fillBoard);

        List<Tile> tiles = board.getAllTiles();
        assertEquals(0, board.getAllTiles().size());

        List<Tile> tilesToPick = List.of(new Tile(TileType.CAT), new Tile(TileType.CAT));
        board.putBackInBag(tilesToPick);
        assertEquals(0, board.getAllTiles().size());

        board.fillBoard();
        assertEquals(1, board.getAllTiles().size());


        assertTrue(game.pickTiles(board.getAllTiles()));
        assertEquals(1, board.getAllTiles().size());
        board.removeFromBoard(board.getAllTiles());
        game.disconnectPlayer(game.getCurrentPlayer());
        assertEquals(1, board.getAllTiles().size());
    }

    @Test
    void startWinnerTimeoutTest() throws PlayerNotFoundException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        List<String> playersName = List.of(p1.getUserName(), p2.getUserName());

        executor.submit(() -> {
            // if first player is disconnected timeout starts immediately
            Game game = new Game(new PushNotificationController(new ArrayList<>()));
            game.setGame(playersName,false, null);
            List<String> players = game.getPlayers();
            game.disconnectPlayer(players.get(0));
            try {
                sleep(21000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(players.get(1), game.getWinner());
        });

        // if second player is disconnected first player should first end his turn, then timeout starts
        executor.submit(() -> {
            // if first player is disconnected timeout starts immediately
            Game game = new Game(new PushNotificationController(new ArrayList<>()));
            game.setGame(playersName,false, null);
            List<String> players = game.getPlayers();
            game.disconnectPlayer(players.get(1));
            try {
                sleep(21000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(null, game.getWinner());
        });

        // if first player reconnects then timeout should be canceled
        executor.submit(() -> {
            // if first player is disconnected timeout starts immediately
            Game game = new Game(new PushNotificationController(new ArrayList<>()));
            game.setGame(playersName,false, null);
            List<String> players = game.getPlayers();
            game.disconnectPlayer(players.get(0));
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            game.reconnectPlayer(players.get(0));
            try {
                sleep(11000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(null, game.getWinner());
        });

        executor.close();
    }
}