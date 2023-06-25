package it.polimi.ingsw.server.model.managers;

import it.polimi.ingsw.server.model.Deck;
import it.polimi.ingsw.server.model.Player;

import java.util.List;
import java.util.Map;

/**
 * A PointsManagers which also manages a deck of cards
 */
public  abstract class CardsPointsManager extends PointsManager {

    protected Deck deck;

    /**
     * Default constructor for CardsPointsManager
     * @param players the players to manage
     * @param deck the deck to manage
     */
    public CardsPointsManager(List<Player> players, Deck deck) {
        super(players);
        this.deck = deck;
    }

    /**
     * Used for deserialization
     * @param players the players
     * @param playersToPoints players to points
     * @param updateRule the update rule that MUST be followed (default ANY)
     * @param deck the deck to manage
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
     * @return the deck
     */
    public Deck getDeck() {
        return deck;
    }
}
