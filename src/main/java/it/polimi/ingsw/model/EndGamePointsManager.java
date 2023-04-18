package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class EndGamePointsManager extends PointsManager{
    private Set<Pattern> patterns;
    public EndGamePointsManager(List<Player> players, Set<Pattern> patterns) {
        super(players);
    }

    public void updatePoints() {}

    public void updatePoints(Player player){
        List<List<Optional<Tile>>> bookshelf = player.getBookShelf().getState();
        Integer newPoints = this.patterns.stream().map(pattern -> pattern.getPatternFunction().apply(bookshelf)).reduce(0, Integer::sum);
        this.playersToPoints.put(player, newPoints);
    }

    public Set<Pattern> getPatterns() {
        return new HashSet<>();
    }
}
