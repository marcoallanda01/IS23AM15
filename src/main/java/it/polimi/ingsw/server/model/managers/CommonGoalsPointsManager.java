package it.polimi.ingsw.server.model.managers;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;

import java.util.*;

public class CommonGoalsPointsManager extends PointsManager {
    private LinkedHashSet<Pattern> patterns;

    public CommonGoalsPointsManager(List<Player> players, LinkedHashSet<Pattern> patterns) {
        super(players);
        this.patterns = patterns;
    }

    /**
     * Used for deserialization
     */
    public CommonGoalsPointsManager(List<Player> players, Map<Player, Integer> playersToPoints, UpdateRule updateRule, LinkedHashSet<Pattern> patterns) {
        super(players, playersToPoints, updateRule);
        this.patterns = patterns;
    }

    /**
     * updates the points of the given player
     * the same bookshelf MUST be used to check every pattern function because
     * if the pattern function wants to remove the tiles that activated it, it should be able to
     * (this is the case, as example, for the adjacent pattern)
     * @param player the player to update
     */
    public void updatePoints(Player player) {
        if (!canBeUpdated(player)) return;
        List<List<Optional<Tile>>> bookshelf = player.getBookShelf().getState();
        Integer newPoints = this.patterns.stream().map(pattern -> pattern.getPatternFunction().apply(bookshelf)).reduce(0, Integer::sum);
        this.playersToPoints.put(player, newPoints);
    }
    /**
     * @return the patterns, the order is not important
     */
    public Set<Pattern> getPatterns() {
        return new HashSet<>(this.patterns);
    }
    /**
     * @param player the player to check
     * @return true if the player can be updated
     */
    protected boolean canBeUpdated(Player player) {
        if(this.players.contains(player)) return true;
        return false;
    }
}
