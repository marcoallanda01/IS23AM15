package it.polimi.ingsw.model;

import java.util.List;

public  abstract class CardsAndPointsManager extends PointsManager {

    protected Deck deck;

    public CardsAndPointsManager(List<Player> players, Deck deck) {
        super(players);
        this.deck = deck;
    }

    public abstract void updatePoints();
    public abstract void updatePoints(Player player);
}
