package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidPatternParameterException;
import it.polimi.ingsw.server.model.managers.CommonCardsPointsManager;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.managers.patterns.SpecificPattern;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonCardsPointsManagerTest {
    @Test
    void constructorTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row3 = List.of(true,false,true);
        List<Boolean> row2 = List.of(false,true,false);
        List<Boolean> row1 = List.of(true, false, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        masks.add(mask);

        Pattern easilyAchievablePattern = new SpecificPattern("MULTICOLOR_X", masks, 1, false, 1, 2);
        Pattern achievablePattern = new SpecificPattern("X", masks, 1, false, 1, 1);
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(easilyAchievablePattern, achievablePattern)));

        Map<Pattern, List<Integer>> ctt = new HashMap<>();
        ctt.put(easilyAchievablePattern, List.of(2,4,6,8));
        ctt.put(achievablePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());
        for(Player player : players)
            assertEquals(0, pm.getPoints(player));
    }
    @Test
    void alternateConstructorTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row3 = List.of(true,false,true);
        List<Boolean> row2 = List.of(false,true,false);
        List<Boolean> row1 = List.of(true, false, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        masks.add(mask);

        Pattern easilyAchievablePattern = new SpecificPattern("MULTICOLOR_X", masks, 1, false, 1, 2);
        Pattern achievablePattern = new SpecificPattern("X", masks, 1, false, 1, 1);
        Pattern unAchievablePattern = new SpecificPattern("1X", masks, 2, false, 1, 1);
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(easilyAchievablePattern, achievablePattern, unAchievablePattern)), 3);

        Map<Pattern, List<Integer>> ctt = new HashMap<>();
        ctt.put(easilyAchievablePattern, List.of(2,4,6,8));
        ctt.put(achievablePattern, List.of(2,4,6,8));
        ctt.put(unAchievablePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());
        for(Player player : players)
            assertEquals(0, pm.getPoints(player));
    }
    @Test
    void fullConstructorTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row3 = List.of(true,false,true);
        List<Boolean> row2 = List.of(false,true,false);
        List<Boolean> row1 = List.of(true, false, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        masks.add(mask);

        Pattern easilyAchievablePattern = new SpecificPattern("MULTICOLOR_X", masks, 1, false, 1, 2);
        Pattern achievablePattern = new SpecificPattern("X", masks, 1, false, 1, 1);
        Pattern unAchievablePattern = new SpecificPattern("1X", masks, 2, false, 1, 1);
        Function<Integer, Stack<Integer>> createStack = (n) -> {
            return java.util.stream.IntStream.rangeClosed(1, n)
                    .boxed()
                    .collect(java.util.stream.Collectors.toCollection(Stack::new));
        };
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(easilyAchievablePattern, achievablePattern, unAchievablePattern)), 3, createStack);

        Map<Pattern, List<Integer>> ctt = new HashMap<>();
        ctt.put(easilyAchievablePattern, List.of(1,2,3,4));
        ctt.put(achievablePattern, List.of(1,2,3,4));
        ctt.put(unAchievablePattern, List.of(1,2,3,4));
        assertEquals(ctt, pm.getCardsToTokens());
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

        Pattern alwaysTruePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern alwaysFalsePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(alwaysTruePattern, alwaysFalsePattern)));

        for(Player player : players)
            assertEquals(0, pm.getPoints(player));
        Map<Pattern, List<Integer>> ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4,6,8));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(0));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4,6));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        // player should not be able to redo the same card twice
        pm.updatePoints(players.get(0));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4,6));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(1));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(2));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(4, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(3));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(4, pm.getPoints(players.get(2)));
        assertEquals(2, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of());
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());
    }
    @Test
    void goalRepetitionTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));

        Pattern alwaysTruePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern alwaysFalsePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(alwaysTruePattern, alwaysFalsePattern)));

        Map<Pattern, List<Integer>> ctt = new HashMap<>();

        pm.updatePoints(players.get(0));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4,6));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        // player should not be able to redo the same card twice
        pm.updatePoints(players.get(0));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4,6));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(1));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(1));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());
    }
    @Test
    void tokenPoppingTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));

        Pattern alwaysTruePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern alwaysFalsePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(alwaysTruePattern, alwaysFalsePattern)));

        Map<Pattern, List<Integer>> ctt = new HashMap<>();

        pm.updatePoints(players.get(0));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4,6));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        // player should not be able to redo the same card twice
        pm.updatePoints(players.get(0));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4,6));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(1));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2,4));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(2));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of(2));
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());

        pm.updatePoints(players.get(3));
        ctt = new HashMap<>();
        ctt.put(alwaysTruePattern, List.of());
        ctt.put(alwaysFalsePattern, List.of(2,4,6,8));
        assertEquals(ctt, pm.getCardsToTokens());
    }
    @Test
    void arePointsUpdatingTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));

        Pattern alwaysTruePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern alwaysFalsePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(alwaysTruePattern, alwaysFalsePattern)));

        pm.updatePoints(players.get(0));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        // player should not be able to redo the same card twice
        pm.updatePoints(players.get(0));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        pm.updatePoints(players.get(1));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        pm.updatePoints(players.get(2));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(4, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        pm.updatePoints(players.get(3));
        assertEquals(8, pm.getPoints(players.get(0)));
        assertEquals(6, pm.getPoints(players.get(1)));
        assertEquals(4, pm.getPoints(players.get(2)));
        assertEquals(2, pm.getPoints(players.get(3)));
    }
    @Test
    void arePlayerTokensUpdatingTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));

        Pattern alwaysTruePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern alwaysFalsePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(alwaysTruePattern, alwaysFalsePattern)));
        Map<Player, List<Integer>> ptt = new HashMap<>();
        ptt.put(players.get(0), List.of());
        ptt.put(players.get(1), List.of());
        ptt.put(players.get(2), List.of());
        ptt.put(players.get(3), List.of());
        assertEquals(ptt, pm.getPlayersToTokens());
        assertEquals(List.of(), pm.getTokens(players.get(0)));
        assertEquals(List.of(), pm.getTokens(players.get(1)));
        assertEquals(List.of(), pm.getTokens(players.get(2)));
        assertEquals(List.of(), pm.getTokens(players.get(3)));

        pm.updatePoints(players.get(0));
        ptt.put(players.get(0), List.of(8));
        ptt.put(players.get(1), List.of());
        ptt.put(players.get(2), List.of());
        ptt.put(players.get(3), List.of());
        assertEquals(ptt, pm.getPlayersToTokens());
        assertEquals(List.of(8), pm.getTokens(players.get(0)));

        pm.updatePoints(players.get(1));
        ptt.put(players.get(0), List.of(8));
        ptt.put(players.get(1), List.of(6));
        ptt.put(players.get(2), List.of());
        ptt.put(players.get(3), List.of());
        assertEquals(ptt, pm.getPlayersToTokens());
        assertEquals(List.of(6), pm.getTokens(players.get(1)));

        pm.updatePoints(players.get(2));
        ptt.put(players.get(0), List.of(8));
        ptt.put(players.get(1), List.of(6));
        ptt.put(players.get(2), List.of(4));
        ptt.put(players.get(3), List.of());
        assertEquals(ptt, pm.getPlayersToTokens());
        assertEquals(List.of(4), pm.getTokens(players.get(2)));

        pm.updatePoints(players.get(3));
        ptt.put(players.get(0), List.of(8));
        ptt.put(players.get(1), List.of(6));
        ptt.put(players.get(2), List.of(4));
        ptt.put(players.get(3), List.of(2));
        assertEquals(ptt, pm.getPlayersToTokens());
        assertEquals(List.of(2), pm.getTokens(players.get(3)));
    }
    @Test
    void arePlayerCardsUpdatingTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));

        Pattern alwaysTruePattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 1;
            }
        };
        Pattern alwaysFalsePattern = new Pattern("alwaysFalse") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonCardsPointsManager pm = new CommonCardsPointsManager(players, new Deck(Set.of(alwaysTruePattern, alwaysFalsePattern)));
        Map<Player, Set<Pattern>> ptt = new HashMap<>();
        ptt.put(players.get(0), Set.of(alwaysTruePattern, alwaysFalsePattern));
        ptt.put(players.get(1), Set.of(alwaysTruePattern, alwaysFalsePattern));
        ptt.put(players.get(2), Set.of(alwaysTruePattern, alwaysFalsePattern));
        ptt.put(players.get(3), Set.of(alwaysTruePattern, alwaysFalsePattern));

        assertEquals(ptt, pm.getPlayersToUnfulfilledCards());
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(0)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(1)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(2)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(3)));

        pm.updatePoints(players.get(0));
        ptt.put(players.get(0), Set.of(alwaysFalsePattern));
        assertEquals(ptt, pm.getPlayersToUnfulfilledCards());
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(0)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(1)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(2)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(3)));

        pm.updatePoints(players.get(1));
        ptt.put(players.get(1), Set.of(alwaysFalsePattern));
        assertEquals(ptt, pm.getPlayersToUnfulfilledCards());
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(0)));
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(1)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(2)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(3)));

        pm.updatePoints(players.get(2));
        ptt.put(players.get(2), Set.of(alwaysFalsePattern));
        assertEquals(ptt, pm.getPlayersToUnfulfilledCards());
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(0)));
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(1)));
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(2)));
        assertEquals(Set.of(), pm.getFulfilledCards(players.get(3)));

        pm.updatePoints(players.get(3));
        ptt.put(players.get(3), Set.of(alwaysFalsePattern));
        assertEquals(ptt, pm.getPlayersToUnfulfilledCards());
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(0)));
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(1)));
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(2)));
        assertEquals(Set.of(alwaysTruePattern), pm.getFulfilledCards(players.get(3)));
    }

}
