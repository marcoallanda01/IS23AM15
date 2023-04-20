package it.polimi.ingsw.server.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalGoalCardManager extends CardsAndPointsManager{
    private Map<Player, Card> playersToCards = new HashMap<>();

    public PersonalGoalCardManager(List<Player> players, Deck deck) {
        super(players, deck);
        // note that the draw operation should not be run in parallel
        players.forEach(player -> playersToCards.put(player, deck.draw()));
    }
    /**
     * updates the points of the given player
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
    public Card getCard(Player player) {
        return playersToCards.get(player);
    }
}
