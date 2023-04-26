package it.polimi.ingsw.server.model;

import java.util.*;

public class EndGamePointsManager extends PointsManager {
    private Set<Pattern> patterns;

    public EndGamePointsManager(List<Player> players, Set<Pattern> patterns) {
        super(players);
        this.patterns = patterns;
    }

    /**
     * Used for deserialization
     */
    public EndGamePointsManager(List<Player> players, Map<Player, Integer> playersToPoints, UpdateRule updateRule, Set<Pattern> patterns) {
        super(players, playersToPoints, updateRule);
        this.patterns = patterns;
    }

    public void updatePoints() {
    }

    public void updatePoints(Player player) {
        List<List<Optional<Tile>>> bookshelf = player.getBookShelf().getState();
        Integer newPoints = this.patterns.stream().map(pattern -> pattern.getPatternFunction().apply(bookshelf)).reduce(0, Integer::sum);
        this.playersToPoints.put(player, newPoints);
    }

    public Set<Pattern> getPatterns() {
        return new HashSet<>();
    }
}
