package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.exceptions.SaveException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.chat.Chat;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.server.model.managers.GoalManager;
import it.polimi.ingsw.server.model.turn.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayControllerTest {

    private Game game;
    private String directory;
    private PlayController playController;
    private List<String> players;

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        game = new Game(players, false);
        directory = "saves";
        playController = new PlayController(game, directory);
    }

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
    void saveGameExceptions() {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "/");
        assertThrows(IOException.class, () -> playController.saveGame("SaveGameTest"));
        PlayController playController1 = new PlayController(game, "saves");
        assertThrows(SaveException.class, () -> playController1.saveGame("SaveGameTest", false));
    }

    @Test
    void getPlayerPoints() throws PlayerNotFoundException {
        assertEquals(0, playController.getPoints(players.get(0)));

        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo"));
        players.add(new Player("pluto"));
        Player player = players.get(0);
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(players.size());
        game = new Game(players, player.getUserName(), false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, "data/goals.json"));

        playController = new PlayController(game, directory);

        assertFalse(player.isFirstToFinish());
        assertEquals(playController.getPoints(player.getUserName()),
                playController.getPoints(players.get(1).getUserName()));

        game = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, "data/goals.json"));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        player.insertTiles(tiles, 0);

        playController = new PlayController(game, directory);

        assertTrue(playController.getPoints(player.getUserName()) >= 0);
    }

    @Test
    void PickAndPutTiles_succeed(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo"));
        players.add(new Player("pluto"));
        Player player = players.get(0);
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(players.size());
        livingRoomBoard.fillBoard();
        List<Tile> tileToPick = new ArrayList<>();
        tileToPick.add(
                livingRoomBoard.getAllTiles().stream()
                        .filter((t)->{
                            List<Tile> tiles = new ArrayList<>();
                            tiles.add(t);
                            return livingRoomBoard.checkPick(tiles);
                        }).findFirst().get()
        );

        game = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, "data/goals.json"));
        playController = new PlayController(game, directory);
        assertTrue(playController.pickTiles(tileToPick, player.getUserName()));
        assertFalse(livingRoomBoard.getAllTiles().contains(tileToPick.get(0)));
        player.gsonPostProcess();
        assertTrue(playController.putTiles(tileToPick, 0, player.getUserName()));
        assertTrue(player.getBookShelf().getAllTiles().contains(tileToPick.get(0)));
    }

    @Test
    void putDifferentTiles(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo"));
        players.add(new Player("pluto"));
        Player player = players.get(0);
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(players.size());
        livingRoomBoard.fillBoard();
        List<Tile> tileToPick = new ArrayList<>();
        tileToPick.add(
                livingRoomBoard.getAllTiles().stream()
                        .filter((t)->{
                            List<Tile> tiles = new ArrayList<>();
                            tiles.add(t);
                            return livingRoomBoard.checkPick(tiles);
                        }).findFirst().get()
        );
        List<Tile> tileToPut = new ArrayList<>();
        List<TileType> typesToPut = new ArrayList<>(List.of(TileType.values()));
        typesToPut.remove(tileToPick.get(0).getType());
        tileToPut.add(new Tile(typesToPut.get(0)));

        game = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, "data/goals.json"));
        playController = new PlayController(game, directory);
        assertTrue(playController.pickTiles(tileToPick, player.getUserName()));
        assertFalse(livingRoomBoard.getAllTiles().contains(tileToPick.get(0)));
        player.gsonPostProcess();
        assertFalse(playController.putTiles(tileToPut, 0, player.getUserName()));
        assertFalse(player.getBookShelf().getAllTiles().contains(tileToPut.get(0)));
    }

    @Test
    void pickTiles_differentPlayer(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo"));
        players.add(new Player("pluto"));
        Player player = players.get(0);
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(players.size());
        livingRoomBoard.fillBoard();
        List<Tile> tileToPick = new ArrayList<>();
        tileToPick.add(
                livingRoomBoard.getAllTiles().stream()
                        .filter((t)->{
                            List<Tile> tiles = new ArrayList<>();
                            tiles.add(t);
                            return livingRoomBoard.checkPick(tiles);
                        }).findFirst().get()
        );

        game = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, "data/goals.json"));
        playController = new PlayController(game, directory);
        assertTrue(playController.pickTiles(tileToPick, player.getUserName()));
        assertFalse(livingRoomBoard.getAllTiles().contains(tileToPick.get(0)));
        player.gsonPostProcess();
        // Wrong turn
        assertFalse(playController.putTiles(tileToPick, 0, players.get(1).getUserName()));
        assertFalse(player.getBookShelf().getAllTiles().contains(tileToPick.get(0)));
        // Player does not exist
        assertFalse(playController.putTiles(tileToPick, 0, "players.get(1).getUserName()"));
    }

    @Test
    void pickTiles_failed(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo"));
        players.add(new Player("pluto"));
        Player player = players.get(0);
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(players.size());
        livingRoomBoard.fillBoard();
        List<Tile> tileToPick = new ArrayList<>();
        tileToPick.add(
                livingRoomBoard.getAllTiles().stream()
                        .filter((t)->{
                            List<Tile> tiles = new ArrayList<>();
                            tiles.add(t);
                            return !livingRoomBoard.checkPick(tiles);
                        }).findFirst().get()
        );
        System.out.println(tileToPick);

        game = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, "data/goals.json"));
        playController = new PlayController(game, directory);
        assertFalse(playController.pickTiles(tileToPick, player.getUserName()));
        assertTrue(livingRoomBoard.getAllTiles().contains(tileToPick.get(0)));
    }

    @Test
    void pickTiles_nickname_failed(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("pippo"));
        players.add(new Player("pluto"));
        Player player = players.get(0);
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(players.size());
        livingRoomBoard.fillBoard();
        List<Tile> tileToPick = new ArrayList<>();
        tileToPick.add(
                livingRoomBoard.getAllTiles().stream()
                        .filter((t)->{
                            List<Tile> tiles = new ArrayList<>();
                            tiles.add(t);
                            return livingRoomBoard.checkPick(tiles);
                        }).findFirst().get()
        );

        game = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, "data/goals.json"));
        playController = new PlayController(game, directory);
        //Wrong turn
        assertFalse(playController.pickTiles(tileToPick, players.get(1).getUserName()));
        assertTrue(livingRoomBoard.getAllTiles().contains(tileToPick.get(0)));
        //No player present
        assertFalse(playController.pickTiles(tileToPick, "players.get(1).getUserName()"));
        assertTrue(livingRoomBoard.getAllTiles().contains(tileToPick.get(0)));
    }

    @Test
    void getPlayerPointsException(){
        String name = "exceptionTest";
        assertFalse(players.contains(name));
        assertThrows(PlayerNotFoundException.class,()-> playController.getPoints(name));
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