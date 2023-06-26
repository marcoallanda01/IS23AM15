package it.polimi.ingsw.server.model.managers;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;

import java.util.*;

/**
 * A PointsManager that handles all the common goals, which are goals that anyone can fulfill,
 * this PointsManager takes into account both the points and the edited bookshelf state returned from the pattern
 */
public class CommonGoalsPointsManager extends PointsManager {
    private LinkedHashSet<Pattern> patterns;

    /**
     * Constructor for CommonGoalsPointsManager, calls super(players) and stores the patterns
     * @param players the players
     * @param patterns the patterns
     */
    public CommonGoalsPointsManager(List<Player> players, LinkedHashSet<Pattern> patterns) {
        super(players);
        this.patterns = patterns;
    }

    /**
     * Used for deserialization
     * @param players the players
     * @param playersToPoints players to points
     * @param updateRule the update rule
     * @param patterns the patterns
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
     * Getter for patterns
     * @return the patterns, when returned the order is not important
     */
    public Set<Pattern> getPatterns() {
        return new HashSet<>(this.patterns);
    }
    /**
     * Checks if a player can be updated
     * @param player the player to check
     * @return true if the player can be updated
     */
    protected boolean canBeUpdated(Player player) {
        if(this.players.contains(player)) return true;
        return false;
    }
}
