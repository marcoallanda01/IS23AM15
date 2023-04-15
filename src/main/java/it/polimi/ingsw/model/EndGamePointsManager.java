package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EndGamePointsManager extends PointsManager{
    public EndGamePointsManager(List<Player> players, Set<Pattern> patterns) {
        super(players);
    }

    public void updatePoints() {}

    public void updatePoints(Player player){}

    public Set<Pattern> getPatterns() {
        return new HashSet<>();
    }
}
