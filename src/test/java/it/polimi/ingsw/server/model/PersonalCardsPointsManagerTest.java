package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalCardsPointsManagerTest {
    @Test
    void constructorTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));
        Pattern always0Pattern = new Pattern("always0Pattern") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        Pattern always1Pattern = new Pattern("always1Pattern") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern always2Pattern = new Pattern("always2Pattern") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 2;
            }
        };
        Pattern always3Pattern = new Pattern("always3Pattern") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 3;
            }
        };
        PersonalCardsPointsManager pm = new PersonalCardsPointsManager(players, new Deck(Set.of(always0Pattern, always1Pattern, always2Pattern, always3Pattern)));
        assertEquals(Set.of(always0Pattern, always1Pattern, always2Pattern, always3Pattern), pm.getPlayersToCards().values().stream().collect(Collectors.toSet()));
        for(Player player : players)
            assertEquals(0, pm.getPoints(player));
    }
    @Test
    void updatePointsTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));
        // conveniently renaming patterns after the number of points they return
        Pattern always0Pattern = new Pattern("0") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        Pattern always1Pattern = new Pattern("1") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern always2Pattern = new Pattern("2") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 2;
            }
        };
        Pattern always3Pattern = new Pattern("3") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 3;
            }
        };
        PersonalCardsPointsManager pm = new PersonalCardsPointsManager(players, new Deck(Set.of(always0Pattern, always1Pattern, always2Pattern, always3Pattern)));

        Integer expectedPoints = Integer.parseInt(pm.getPlayersToCards().get(players.get(0)).getName()); // this works because of convenient pattern names
        pm.updatePoints(players.get(0));
        assertEquals(expectedPoints, pm.getPoints(players.get(0)));

        expectedPoints = Integer.parseInt(pm.getPlayersToCards().get(players.get(0)).getName()); // this works because of convenient pattern names
        pm.updatePoints(players.get(0));
        assertEquals(expectedPoints, pm.getPoints(players.get(0)));

        expectedPoints = Integer.parseInt(pm.getPlayersToCards().get(players.get(1)).getName()); // this works because of convenient pattern names
        pm.updatePoints(players.get(1));
        assertEquals(expectedPoints, pm.getPoints(players.get(1)));

        expectedPoints = Integer.parseInt(pm.getPlayersToCards().get(players.get(2)).getName()); // this works because of convenient pattern names
        pm.updatePoints(players.get(2));
        assertEquals(expectedPoints, pm.getPoints(players.get(2)));

        expectedPoints = Integer.parseInt(pm.getPlayersToCards().get(players.get(3)).getName()); // this works because of convenient pattern names
        pm.updatePoints(players.get(3));
        assertEquals(expectedPoints, pm.getPoints(players.get(3)));
    }
    @Test
    void getCardTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));
        // conveniently renaming patterns after the number of points they return
        Pattern always0Pattern = new Pattern("0") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        Pattern always1Pattern = new Pattern("1") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern always2Pattern = new Pattern("2") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 2;
            }
        };
        Pattern always3Pattern = new Pattern("3") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 3;
            }
        };
        PersonalCardsPointsManager pm = new PersonalCardsPointsManager(players, new Deck(Set.of(always0Pattern, always1Pattern, always2Pattern, always3Pattern)));

        assertEquals(pm.getPlayersToCards().get(players.get(0)), pm.getCard(players.get(0)));
        assertEquals(pm.getPlayersToCards().get(players.get(1)), pm.getCard(players.get(1)));
        assertEquals(pm.getPlayersToCards().get(players.get(2)), pm.getCard(players.get(2)));
        assertEquals(pm.getPlayersToCards().get(players.get(3)), pm.getCard(players.get(3)));


    }

}
