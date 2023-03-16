package it.polimi.ingsw.modules;

import java.util.List;

public abstract class PointsManager {
    protected List<Player> players;

    public PointsManager(List<Player> players) {
        this.players = players;
    }

    public abstract void updatePoints();
}
