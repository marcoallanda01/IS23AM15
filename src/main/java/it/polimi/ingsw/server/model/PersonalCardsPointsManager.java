package it.polimi.ingsw.server.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalCardsPointsManager extends CardsPointsManager {
    private final Map<Player, Pattern> playersToCards = new HashMap<>();

    public PersonalCardsPointsManager(List<Player> players, Deck deck) {
        super(players, deck);
        // note that the draw operation should not be run in parallel
        players.forEach(player -> playersToCards.put(player, deck.draw()));
    }

    /**
     * Used for deserialization
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
        Integer newPoints = this.playersToCards.get(player).getPatternFunction().apply(player.getBookShelf().getState());
        this.playersToPoints.put(player, newPoints);
    }

    /**
     * @param player the player
     * @return the personal card of the player
     */
    public Pattern getCard(Player player) {
        return playersToCards.get(player);
    }

    /**
     * Used for serialization
     */
    public Map<Player, Pattern> getPlayersToCards() {
        return playersToCards;
    }

}