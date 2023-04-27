package it.polimi.ingsw.server.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PointsManager {
    protected List<Player> players;
    protected Map<Player, Integer> playersToPoints;
    protected UpdateRule updateRule = UpdateRule.ANY;

    public PointsManager(List<Player> players) {
        this.players = players;
        playersToPoints = new HashMap<>();
        players.forEach(player -> this.playersToPoints.put(player, 0));
    }

    /**
     * Used for deserialization
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
     * @param player the player
     * @return the points of the player
     */
    public Integer getPoints(Player player) {
        return playersToPoints.get(player);
    }
    /**
     * @return the frequency to which the PointsManager MUST be updated
     */
    public UpdateRule getUpdateRule() { return updateRule; }



    /**
     * Used for serialization
     */
    public Map<Player, Integer> getPlayersToPoints() {
        return playersToPoints;
    }

}
