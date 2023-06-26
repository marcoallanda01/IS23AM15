package it.polimi.ingsw.server.model.managers;

import it.polimi.ingsw.server.model.Deck;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A PointsManager that handles all the personal cards, which are goals that a player can fulfill multiple times,
 * note that points are not summed, but the latest fulfillment will be taken into account,
 * this PointsManager takes into account the points but not the edited bookshelf state returned from the pattern
 */
public class PersonalCardsPointsManager extends CardsPointsManager {
    private final Map<Player, Pattern> playersToCards = new HashMap<>();

    /**
     * Constructor for PersonalCardsPointsManager which draws a card for each player and assigns it to each one
     * @param players the players
     * @param deck the deck of cards
     */
    public PersonalCardsPointsManager(List<Player> players, Deck deck) {
        super(players, deck);
        // note that the draw operation should not be run in parallel
        players.forEach(player -> playersToCards.put(player, deck.draw()));
    }


    /**
     * Used for deserialization
     * @param players the players
     * @param playersToPoints players to points
     * @param updateRule the update rule
     * @param deck the deck
     * @param playersToCards players to cards
     */
    public PersonalCardsPointsManager(List<Player> players, Map<Player, Integer> playersToPoints, UpdateRule updateRule, Deck deck, Map<Player, Pattern> playersToCards) {
        super(players, playersToPoints, updateRule, deck);
        this.playersToCards.putAll(playersToCards);
    }

    /**
     * updates the points of the given player
     *
     * @param player the player to update
     */
    public void updatePoints(Player player) {
        if (!canBeUpdated(player)) return;
        Integer newPoints = this.playersToCards.get(player).getPatternFunction().apply(player.getBookShelf().getState());
        this.playersToPoints.put(player, newPoints);
    }

    /**
     * Getter for the card of the player
     * @param player the player
     * @return the personal card of the player
     */
    public Pattern getCard(Player player) {
        return playersToCards.get(player);
    }

    /**
     * Used for serialization
     * @return players to cards
     */
    public Map<Player, Pattern> getPlayersToCards() {
        return new HashMap<>(playersToCards);
    }
    /**
     * @param player the player to check
     * @return true if the player can be updated
     */
    protected boolean canBeUpdated(Player player) {
        if(this.players.contains(player) && this.playersToCards.containsKey(player)) return true;
        return false;
    }
}
