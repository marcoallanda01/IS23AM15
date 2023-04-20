package it.polimi.ingsw.server.model;

import java.util.List;

public  abstract class CardsAndPointsManager extends PointsManager {

    protected Deck deck;

    public CardsAndPointsManager(List<Player> players, Deck deck) {
        super(players);
        this.deck = deck;
    }
    /**
     * updates the points of the given player
     * @param player the player to update
     */
    public abstract void updatePoints(Player player);
}
