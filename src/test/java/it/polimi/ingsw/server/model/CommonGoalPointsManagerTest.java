package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidPatternParameterException;
import it.polimi.ingsw.server.model.managers.CommonGoalsPointsManager;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.managers.patterns.SpecificPattern;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonGoalPointsManagerTest {

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
        LinkedHashSet<Pattern> patterns = new LinkedHashSet<>(List.of(easilyAchievablePattern, achievablePattern));
        CommonGoalsPointsManager pm = new CommonGoalsPointsManager(players, patterns);
        assertEquals(patterns, pm.getPatterns());
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

        Pattern always3Pattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 3;
            }
        };
        Pattern always0Pattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonGoalsPointsManager pm = new CommonGoalsPointsManager(players, new LinkedHashSet<>(List.of(always3Pattern, always0Pattern)));

        for(Player player : players)
            assertEquals(0, pm.getPoints(player));

        pm.updatePoints(players.get(0));
        assertEquals(3, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        // points are recalculated, not added
        pm.updatePoints(players.get(0));
        assertEquals(3, pm.getPoints(players.get(0)));
        assertEquals(0, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        pm.updatePoints(players.get(1));
        assertEquals(3, pm.getPoints(players.get(0)));
        assertEquals(3, pm.getPoints(players.get(1)));
        assertEquals(0, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        pm.updatePoints(players.get(2));
        assertEquals(3, pm.getPoints(players.get(0)));
        assertEquals(3, pm.getPoints(players.get(1)));
        assertEquals(3, pm.getPoints(players.get(2)));
        assertEquals(0, pm.getPoints(players.get(3)));

        pm.updatePoints(players.get(3));
        assertEquals(3, pm.getPoints(players.get(0)));
        assertEquals(3, pm.getPoints(players.get(1)));
        assertEquals(3, pm.getPoints(players.get(2)));
        assertEquals(3, pm.getPoints(players.get(3)));
    }
    @Test
    void getPatternsTest() throws InvalidPatternParameterException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("user1"));
        players.add(new Player("user2"));
        players.add(new Player("user3"));
        players.add(new Player("user4"));

        Pattern always3Pattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 3;
            }
        };
        Pattern always0Pattern = new Pattern("alwaysTrue") {
            @Override
            public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
                return (bookshelf) -> 0;
            }
        };
        CommonGoalsPointsManager pm = new CommonGoalsPointsManager(players, new LinkedHashSet<>(List.of(always3Pattern, always0Pattern)));

        LinkedHashSet<Pattern> expectedPatterns = new LinkedHashSet<>(List.of(always3Pattern, always0Pattern));
        assertEquals(expectedPatterns, pm.getPatterns());

        for(Player player : players)
            assertEquals(0, pm.getPoints(player));

        pm.updatePoints(players.get(0));
        assertEquals(expectedPatterns, pm.getPatterns());

        pm.updatePoints(players.get(0));
        assertEquals(expectedPatterns, pm.getPatterns());

        pm.updatePoints(players.get(1));
        assertEquals(expectedPatterns, pm.getPatterns());

        pm.updatePoints(players.get(2));
        assertEquals(expectedPatterns, pm.getPatterns());

        pm.updatePoints(players.get(3));
        assertEquals(expectedPatterns, pm.getPatterns());

    }
}
