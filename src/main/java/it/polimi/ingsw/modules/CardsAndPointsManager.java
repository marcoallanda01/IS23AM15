package it.polimi.ingsw.modules;

import java.util.List;

public  abstract class CardsAndPointsManager extends PointsManager {

    public CardsAndPointsManager(List<Player> players, Deck deck) {
        super(players);

    }

    public abstract void updatePoints();
}
