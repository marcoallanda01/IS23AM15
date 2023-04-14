package it.polimi.ingsw.modules;
import java.util.List;
import java.util.Map;

public abstract class PointsManager {
    protected List<Player> players;
    protected Map<Player, Integer> playersToPoints;

    public PointsManager(List<Player> players) {
        this.players = players;
        players.forEach(player -> this.playersToPoints.put(player, 0));
    }

    public abstract void updatePoints();
    public abstract void updatePoints(Player player);
    public Integer getPlayerPoints(Player player) {
        return playersToPoints.get(player);
    }

}
