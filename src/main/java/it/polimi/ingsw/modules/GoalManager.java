package it.polimi.ingsw.modules;

import java.util.*;

public class GoalManager {
    private List<Player> players;
    private List<PointsManager> pointsManagers = new ArrayList<>();

    private CommonGoalCardManager commonGoalCardManager;
    private PersonalGoalCardManager personalGoalCardManager;
    private EndGamePointsManager endGamePointsManager;

    private String setUpFile;

    public GoalManager(List<Player> players, String setUpFile) {
        this.players = new ArrayList<Player>(players);
        this.setUpFile = setUpFile;

        // I use list and not set because I could choose to have to same card so the probability increase
        List<Pattern> commonGoalCardPatternsToNames = new ArrayList<Pattern>();
        List<Pattern> personalGoalCardManagerPatterns = new ArrayList<Pattern>();

        // Just only one pattern for every goal that is in common and all are active at the same time
        Set<Pattern> EndGamePointsManagerPatterns = new HashSet<Pattern>();

        // creating default managers, in future to add another manager add it here
        this.commonGoalCardManager = new CommonGoalCardManager(players, new Deck(commonGoalCardPatternsToNames));
        this.personalGoalCardManager = new PersonalGoalCardManager(players, new Deck(personalGoalCardManagerPatterns));
        this.endGamePointsManager = new EndGamePointsManager(players, EndGamePointsManagerPatterns);

        this.pointsManagers.add(this.commonGoalCardManager);
        this.pointsManagers.add(this.personalGoalCardManager);
        this.pointsManagers.add(this.endGamePointsManager);
    }

    public void updatePointsTurn() {
        // NB: updatePointsTurn() can't actually update the points for each PointsManager
        // this issue is caused from the fact that points are stored in the player and
        // therefore are supposed to be updated just once (at the end of the game)
        // this function actually just updates the CommonGoalCardManager (which needs to be updated every turn)
        // (but does not update the points relative to the CommonGoalCardManager!, just its state)
        // a updatePointsTurn will be added to every manager, if points will be moved
        // to the manager itself this will allow to display updated player points every turn
        // and not just at the end of the game
        pointsManagers.forEach(pointsmanager -> pointsmanager.updatePointsTurn());
    }

    public void updatePointsEnd() {
        pointsManagers.forEach(pointsmanager -> pointsmanager.updatePoints());
    }

    // good for now, might want to clone or send a simplified version of these objects for security reasons (again)
    /**
     * @return a map associating cards to tokens
     */
    // this method does not take a card as input because cards are handled exclusively  by CardsManagers
    public Map<Card, Stack<Token>> getCommonCardsToTokens() {
        return commonGoalCardManager.getCardsToTokens();
    }
    /**
     * @param player the player
     * @return the tokens of the player:
     */
    public Set<Token> getPlayerTokens(Player player) {
        return commonGoalCardManager.getPlayerTokens(player);
    }
    /**
     * @param player the player
     * @return the unfulfilled cards of the player
     */
    public Set<Card> getPlayerCommonUnfulfilledCards(Player player) {
        return commonGoalCardManager.getPlayerUnfulfilledCards(player);
    }
    /**
     * @param player the player
     * @return the fulfilled cards of the player
     */
    public Set<Card> getPlayerCommonFulfilledCards(Player player) {
        return commonGoalCardManager.getPlayerFulfilledCards(player);
    }
    /**
     * @param player the player
     * @return the personal card of the player
     */
    public Card getPlayerPersonalCards(Player player) {
        return personalGoalCardManager.getPlayerCard(player);
    }
}
