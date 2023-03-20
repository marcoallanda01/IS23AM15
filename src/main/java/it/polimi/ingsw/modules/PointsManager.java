package it.polimi.ingsw.modules;

import java.util.List;

public abstract class PointsManager {
    protected List<Player> players;

    public PointsManager(List<Player> players) {
        this.players = players;
    }

    public abstract void updatePoints();

    // this does not need to be implemented from every PointsManager,
    // some PointsManagers might only want to update points at the end of the game
    public void updatePointsTurn() {}

}
