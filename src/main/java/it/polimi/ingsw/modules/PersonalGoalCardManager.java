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
        players.stream().forEach(player -> updatePoints(player));
    }
    /**
     * updates the points of the given player
     * @param player the player to update
     */
    public void updatePoints(Player player) {
        Integer oldPoints = this.playersToPoints.get(player);
        Integer newPoints = this.playersToCards.get(player).getPatternFunction().apply(player.getBookShelf().getState());
        // update the points internal to the manager
        this.playersToPoints.put(player, newPoints);
        // update the points in the player
        player.addPoints(newPoints - oldPoints);
    }
    /**
     * @param player the player
     * @return the personal card of the player
     */
    public Card getPlayerCard(Player player) {
        return playersToCards.get(player);
    }
}
