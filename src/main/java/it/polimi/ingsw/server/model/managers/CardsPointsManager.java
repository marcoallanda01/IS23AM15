package it.polimi.ingsw.server.model.managers;

import it.polimi.ingsw.server.model.Deck;
import it.polimi.ingsw.server.model.Player;

import java.util.List;
import java.util.Map;

public  abstract class CardsPointsManager extends PointsManager {

    protected Deck deck;

    public CardsPointsManager(List<Player> players, Deck deck) {
        super(players);
        this.deck = deck;
    }

    /**
     * Used for deserialization
     */
    public CardsPointsManager(List<Player> players, Map<Player, Integer> playersToPoints, UpdateRule updateRule, Deck deck) {
        super(players, playersToPoints, updateRule);
        this.deck = deck;
    }
    /**
     * updates the points of the given player
     * @param player the player to update
     */
    public abstract void updatePoints(Player player);


    /**
     * Used for serialization
     */
    public Deck getDeck() {
        return deck;
    }
}
