package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GoalManagerTest {


    @Test
    void constructorTest(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        GoalManager gm = new GoalManager(players, "data/goals.json");
    }


    @Test
    void updatePointsTurn() {
    }

    @Test
    void updatePointsEnd() {
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