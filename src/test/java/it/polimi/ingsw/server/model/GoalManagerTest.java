package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GoalManagerTest {
    GoalManager goalManager;
    List<Player> players;
    @BeforeEach
    void constructorTest(){
        players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        goalManager = new GoalManager(players, "data/goals.json");
    }


    @Test
    void updatePointsTurn() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles, 1);
        goalManager.updatePointsTurn(players.get(1));
        assertEquals(0, goalManager.getPoints(players.get(1)));
        players.get(1).insertTiles(tiles, 3);
        players.get(1).insertTiles(tiles, 4);
        goalManager.updatePointsTurn(players.get(1));
        assertNotEquals(2, goalManager.getPoints(players.get(1)));
    }

    @Test
    void updatePointsEnd() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        tiles.add(new Tile(-1, -1, TileType.CAT));
        players.get(1).insertTiles(tiles, 1);
        goalManager.updatePointsEnd(players.get(1));
        assertEquals(2, goalManager.getPoints(players.get(1)));
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
        assertEquals(10, goalManager.getPoints(players.get(1)));
    }

    @Test
    void getPoints() {
    }

    @Test
    void getCommonCardsToTokens() {
    }

    @Test
    void getTokens() {
    }

    @Test
    void getUnfulfilledCommonCards() {
    }

    @Test
    void getFulfilledCommonCards() {
    }

    @Test
    void getPersonalCard() {
    }

    @Test
    void getEndGameGoals() {
    }

    @Test
    void getCommonGoalCardManager() {
    }

    @Test
    void getPersonalGoalCardManager() {
    }

    @Test
    void getEndGamePointsManager() {
    }

    @Test
    void getFrequentUpdates() {
    }
}