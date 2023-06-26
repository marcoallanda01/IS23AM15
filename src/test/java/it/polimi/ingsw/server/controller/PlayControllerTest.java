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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PlayControllerTest {

    private Game game;
    private String directory;
    private PlayController playController;
    private List<String> players;
    private PushNotificationController pnc;
    private String goalmangerFile = "data/goals.json";

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        pnc = new PushNotificationController(new ArrayList<>());
        game = new Game(pnc);
        game.setGame(players, false, null);
        directory = "src/test/resources/saves";
        playController = new PlayController(game, directory, pnc);
    }

    @Test
    void saveGame() throws IOException, SaveException {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "src/test/resources/saves", pnc);
        assertTrue(playController.saveGame("SaveGameTest", true));
    }

    @Test
    void saveGameExceptions() {
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "/", pnc);
        assertThrows(IOException.class, () -> playController.saveGame("SaveGameTest"));
        PlayController playController1 = new PlayController(game, "src/test/resources/saves", pnc);
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
                new Chat(players), new GoalManager(players, goalmangerFile, true, true));

        playController = new PlayController(game, directory, pnc);

        assertFalse(player.isFirstToFinish());
        assertEquals(playController.getPoints(player.getUserName()),
                playController.getPoints(players.get(1).getUserName()));

        game = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, goalmangerFile, false, true));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        player.insertTiles(tiles, 0);

        playController = new PlayController(game, directory, pnc);

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

        game.setGame( new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, goalmangerFile, true, true)) );
        assertTrue(playController.pickTiles(tileToPick, player.getUserName()));
        assertFalse(livingRoomBoard.getAllTiles().contains(tileToPick.get(0)));
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

        game.setGame( new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, goalmangerFile, true, true)));
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

        Game game2 = new Game(players, null, false, livingRoomBoard, new Turn(player, livingRoomBoard),
                new Chat(players), new GoalManager(players, goalmangerFile, true, true));
        game.setGame(game2);
        playController = new PlayController(game, directory, pnc);
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
                new Chat(players), new GoalManager(players, goalmangerFile, true, true));
        playController = new PlayController(game, directory, pnc);
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
                new Chat(players), new GoalManager(players, goalmangerFile, true, true));
        playController = new PlayController(game, directory, pnc);
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
        PlayController playController = new PlayController(game, "src/test/resources/saves", pnc);
        assertTrue(playController.leave("player1"));
        assertEquals(1, game.getPlayersList().stream().filter(p -> !p.isPlaying()).count());
        assertFalse(playController.leave("player1"));
        assertEquals(1, game.getPlayersList().stream().filter(p -> !p.isPlaying()).count());
        assertFalse(playController.leave("player4"));
    }

    @Test
    void isPlaying(){
        assertTrue(playController.isPlaying("player2"));
        assertFalse(playController.isPlaying("player33432"));
    }

    @Test
    void reconnect(){
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        players.add("player3");
        Game game = new Game(players, false);
        PlayController playController = new PlayController(game, "src/test/resources/saves", pnc);
        assertTrue(playController.isPlaying("player1"));
        assertFalse(playController.reconnect("player1"));
        assertTrue(playController.isPlaying("player1"));
        assertTrue(playController.leave("player1"));
        assertFalse(playController.isPlaying("player1"));
        assertTrue(playController.reconnect("player1"));
        assertTrue(playController.isPlaying("player1"));
        assertEquals(0, game.getPlayersList().stream().filter(p -> !p.isPlaying()).count());
        assertFalse(playController.reconnect("player4"));
    }

    @Test
    void getCommonGoalCardsToTokens(){
        assertEquals(playController.getCommonCardsToTokens(), game.getCommonCardsToTokens());
    }

    @Test
    void getEndGameGoals(){
        assertEquals(playController.getCommonGoals(), game.getCommonGoals());
    }

    @Test
    void getUnfulfilledCommonGoalCards() throws PlayerNotFoundException {
        String player = players.get(1);
        String noPlayer = "throw";
        assertFalse(player.contains(noPlayer));
        assertThrows(PlayerNotFoundException.class, () -> playController.getUnfulfilledCommonCards(noPlayer));
        assertEquals(playController.getUnfulfilledCommonCards(player), game.getUnfulfilledCommonCards(player));
    }

    @Test
    void getFulfilledCommonGoalCards() throws PlayerNotFoundException {
        String player = players.get(1);
        String noPlayer = "throw";
        assertFalse(player.contains(noPlayer));
        assertThrows(PlayerNotFoundException.class, () -> playController.getFulfilledCommonCards(noPlayer));
        assertEquals(playController.getFulfilledCommonCards(player), game.getFulfilledCommonCards(player));
    }

    @Test
    void getPersonalGoalCard() throws PlayerNotFoundException {
        String player = players.get(1);
        String player2 = players.get(2);
        String noPlayer = "throw";
        assertFalse(player.contains(noPlayer));
        assertThrows(PlayerNotFoundException.class, () -> playController.getPersonalGoalCard(noPlayer));
        assertEquals(playController.getPersonalGoalCard(player), game.getPersonalGoalCard(player));
        assertNotEquals(playController.getPersonalGoalCard(player), playController.getPersonalGoalCard(player2));
    }

    @Test
    void getTokens() throws PlayerNotFoundException {
        String player = players.get(1);
        String noPlayer = "throw";
        assertFalse(player.contains(noPlayer));
        assertThrows(PlayerNotFoundException.class, () -> playController.getTokens(noPlayer));
        assertEquals(playController.getTokens(player), game.getTokens(player));
        assertEquals(playController.getTokens(player).size(), playController.getFulfilledCommonCards(player).size());
        LivingRoomBoard livingRoomBoard = game.getBoard();
        List<Tile> tileToPick = new ArrayList<>();
        tileToPick.add(
                livingRoomBoard.getAllTiles().stream()
                        .filter((t)->{
                            List<Tile> tiles = new ArrayList<>();
                            tiles.add(t);
                            return livingRoomBoard.checkPick(tiles);
                        }).findFirst().get()
        );
        playController.pickTiles(tileToPick, player);
        assertEquals(playController.getTokens(player).size(), playController.getFulfilledCommonCards(player).size());
    }

    @Test
    void getWinner(){

        assertNull(playController.getWinner());

        String winner = players.get(1);
        List<Player> Players = players.stream().map(Player::new).toList();
        game = new Game(Players, winner, false, game.getBoard(), new Turn(Players.get(1), game.getBoard()),
                new Chat(Players), new GoalManager(Players, goalmangerFile, true, true));
        playController = new PlayController(game, directory, pnc);
        assertEquals(winner, playController.getWinner());
        assertEquals(winner, game.getWinner());
    }

    @Test
    void getPlayers(){
        assertTrue(playController.getPlayers().containsAll(players));
        assertTrue(players.containsAll(playController.getPlayers()));
        assertEquals(playController.getPlayers(), game.getPlayers());
    }

    @Test
    void getBookShelf() throws PlayerNotFoundException {
        assertThrows(PlayerNotFoundException.class, ()->playController.getBookshelf("NotExists"));
        assertEquals(0,playController.getBookshelf("player1").size());
        assertEquals(0,playController.getBookshelf("player3").size());
    }

    @Test
    void getBoard(){
        assertNotNull(playController.getBoard());
    }

    @Test
    void getCurrentPlayer(){
        assertTrue(players.contains(playController.getCurrentPlayer()));
    }
}