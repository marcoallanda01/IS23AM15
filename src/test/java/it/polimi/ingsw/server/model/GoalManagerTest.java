package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.ArrestGameException;
import it.polimi.ingsw.server.model.managers.CommonCardsPointsManager;
import it.polimi.ingsw.server.model.managers.CommonGoalsPointsManager;
import it.polimi.ingsw.server.model.managers.GoalManager;
import it.polimi.ingsw.server.model.managers.PersonalCardsPointsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoalManagerTest {
    GoalManager goalManager;
    List<Player> players;
    String goalmangerFile = "data/goals.json";
    @BeforeEach
    void setPlayers(){
        players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
    }


    @Test
    void updatePointsTurn() {
        goalManager = new GoalManager(players, "data/goals.json", true, true);
        goalManager.updatePointsTurn(null);
        assertThrows(NullPointerException.class, () -> goalManager.updatePointsTurn(new Player("altro")));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles, 1);
        goalManager.updatePointsTurn(players.get(1));
        assertTrue(0 <= goalManager.getPoints(players.get(2)));
        assertEquals(0, goalManager.getPoints(players.get(2)));
        players.get(1).insertTiles(tiles, 3);
        players.get(1).insertTiles(tiles, 4);
        goalManager.updatePointsTurn(players.get(1));
    }

    @Test
    void updatePointsEnd() {
        goalManager = new GoalManager(players, goalmangerFile, true, true);
        goalManager.updatePointsEnd(null);
        // If I do the update points of a player that not exists managers do nothing
        goalManager.updatePointsEnd(new Player("altro"));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles, 1);
        goalManager.updatePointsEnd(players.get(1));
        assertTrue(goalManager.getPoints(players.get(1)) >= 2);
        List<Tile> tiles2 = new ArrayList<>();
        tiles2.add(new Tile(-1, -1, TileType.CAT));
        tiles2.add(new Tile(-1, -1, TileType.CAT));
        tiles2.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles2, 3);
        List<Tile> tiles3 = new ArrayList<>();
        tiles3.add(new Tile(-1, -1, TileType.CAT));
        tiles3.add(new Tile(-1, -1, TileType.CAT));
        tiles3.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles3, 4);
        goalManager.updatePointsEnd(players.get(1));
        assertNotEquals(2, goalManager.getPoints(players.get(1)));
    }

    @Test
    void getPoints() {
        goalManager = new GoalManager(players, goalmangerFile, true, true);
        assertEquals(0, goalManager.getPoints(players.get(1)));
    }

    @Test
    void getWinner(){
        goalManager = new GoalManager(players, goalmangerFile, true, true);
        assertEquals(players.get(0).getUserName(), goalManager.getWinner(players));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles, 1);
        assertEquals(players.get(1).getUserName(), goalManager.getWinner(players));
    }

    @Test
    void getCommonCardsToTokens() {
        goalManager = new GoalManager(players, goalmangerFile, true, true);
        System.out.println(goalManager.getCommonCardsToTokens());
    }

    @Test
    void getTokens() {
        goalManager = new GoalManager(players, goalmangerFile, true, true);
        System.out.println(goalManager.getTokens(players.get(1)));
    }

    @Test
    void getUnfulfilledCommonCards() {
        goalManager = new GoalManager(players, goalmangerFile, true, true);
        System.out.println(goalManager.getTokens(players.get(1)));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles, 1);
        System.out.println(goalManager.getTokens(players.get(1)));
    }

    @Test
    void personalCardsXY(){
        goalManager = new GoalManager(players, "tests/test_personalCardsXYRight.json", true, true);
        Player player = players.get(0);
        // [0,0,"PLANT"], [0,2,"FRAME"], [1,5,"CAT"], [2,4,"BOOK"], [3,1,"GAME"], [5,2,"TROPHIE"]
        List<Tile> personalCard = new ArrayList<>();
        personalCard.add(new Tile(0, 1, TileType.BOOK));
        personalCard.add(new Tile(0, 1, TileType.PLANT));
        player.insertTiles(personalCard, 0);
        System.out.println(player.getBookShelf());
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));
        assertThrows(ArrestGameException.class,
                () -> new GoalManager(players, "tests/test_personalCardsXYWrong.json", true, true));
        // expected a sting as name
        assertThrows(ArrestGameException.class,
                () -> new GoalManager(players, "tests/test_personalCardsXYWrong.json", true, true));

    }

    @Test
    void noEnoughPersonalCards(){
        //0 cards
        assertThrows(ArrestGameException.class,
                () -> new GoalManager(players, "tests/test_personalCardsNoCards.json", true, true));
        // 2 cards only with 3 players
        assertThrows(ArrestGameException.class,
                () -> new GoalManager(players, "tests/test_personalCards2Cards.json", true, true));

    }


    @Test
    void noEnoughCommonCards(){
        // there are three cards wrong
        //1 card needed
        new GoalManager(players, "tests/test_commonCardsNotEnough.json", true,true);
        //1 card needed 2
        assertThrows(ArrestGameException.class,
                () -> new GoalManager(players, "tests/test_commonCardsNotEnough.json", false, true));

    }

    @Test
    void noEndGoals(){
        //EndGoals are not mandatory
        new GoalManager(players, "tests/test_noEndGoals.json", true,true);
    }

    @Test
    void errorsInEndGoals(){
        //If and end goals is wrong no problems
        //Only 4_ADJACENT should be printed for endGoals
        new GoalManager(players, "tests/test_errorsInEnd.json", true,true);
    }

    private GoalManager setGoalMangerForCommonCardsSafeTest(String file){
        return new GoalManager(players, file, true,true);
    }


    @Test
    void FailedToOpenException(){
        assertThrows(ArrestGameException.class,
                () -> new GoalManager(players, "fileNonEsistente", true, true));
    }

    @Test
    void wrongFormatted(){
        assertThrows(ArrestGameException.class,
                () -> new GoalManager(players, "tests/test_wrongFormatted.json", true,true));
    }


    @Test
    void testCommonCardsAreSafe(){
        Player player = players.get(0);
        // [0,0,"PLANT"], [0,2,"FRAME"], [1,5,"CAT"], [2,4,"BOOK"], [3,1,"GAME"], [5,2,"TROPHIE"]
        List<Tile> personalCard = new ArrayList<>();
        personalCard.add(new Tile(0, 1, TileType.BOOK));
        personalCard.add(new Tile(0, 1, TileType.PLANT));
        player.insertTiles(personalCard, 0);
        System.out.println(player.getBookShelf());

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_1.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_2.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_3.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_4.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_5.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_6.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_7.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_8.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_9.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_10.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_12.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));

        goalManager = setGoalMangerForCommonCardsSafeTest("tests/test_CommonCardsSafe_11.json");
        goalManager.updatePointsTurn(player);
        goalManager.updatePointsEnd(player);
        assertEquals(1, goalManager.getPoints(player));
    }
}