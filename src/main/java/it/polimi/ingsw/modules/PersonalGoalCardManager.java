package it.polimi.ingsw.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalGoalCardManager extends CardsAndPointsManager{
    private Map<Player, Card> playersToCards = new HashMap<>();

    public PersonalGoalCardManager(List<Player> players, Deck deck) {
        super(players, deck);
        // note that the draw operation should not be run in parallel
        players.stream().forEach(player -> playersToCards.put(player, deck.draw()));
    }
    /**
     * Updates the points of each player
     */
    public void updatePoints() {
        players.stream().forEach(player -> updatePlayerPoints(player));
    }
    /**
     * updates the points of the given player
     * @param player the player to update
     */
    public void updatePlayerPoints(Player player) {
        player.addPoints(this.playersToCards.get(player).getPatternFunction().apply(player.getBookShelf().getState()));
    }
    /**
     * @param player the player
     * @return the personal card of the player
     */
    public Card getPlayerCard(Player player) {
        return playersToCards.get(player);
    }
}
