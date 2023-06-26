package it.polimi.ingsw.server.model.managers;
import it.polimi.ingsw.server.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a PointsManager that assigns points based on the state of the player and its bookshelf
 * PointsManagers can have additional parameters and the assigned points can depend on other things such as Patterns
 */
public abstract class PointsManager {
    protected List<Player> players;
    protected Map<Player, Integer> playersToPoints;
    protected UpdateRule updateRule = UpdateRule.ANY;

    /**
     * Constructor for PointsManager, just creates a map player to points and sets the starting points at 0
     * @param players the players
     */
    public PointsManager(List<Player> players) {
        this.players = players;
        playersToPoints = new HashMap<>();
        players.forEach(player -> this.playersToPoints.put(player, 0));
    }

    /**
     * Used for deserialization
     * @param players the players
     * @param playersToPoints players to points
     * @param updateRule the update rule that MUST be followed (default ANY)
     */
    public PointsManager(List<Player> players, Map<Player, Integer> playersToPoints, UpdateRule updateRule) {
        this.players = players;
        this.playersToPoints = playersToPoints;
        this.updateRule = updateRule;
    }
    /**
     * updates the points of the given player
     * @param player the player to update
     */
    public abstract void updatePoints(Player player);
    /**
     * Gets the points of the player
     * @param player the player
     * @return the points of the player
     */
    public Integer getPoints(Player player) {
        return playersToPoints.get(player);
    }
    /**
     * Gets the update rule
     * @return the frequency to which the PointsManager MUST be updated
     */
    public UpdateRule getUpdateRule() { return updateRule; }

    /**
     * Used for serialization
     * @return playersToPoints
     */
    public Map<Player, Integer> getPlayersToPoints() {
        return playersToPoints;
    }
    /**
     * @param player the player to check
     * @return true if the player can be updated
     */
    protected abstract boolean canBeUpdated(Player player);
}
