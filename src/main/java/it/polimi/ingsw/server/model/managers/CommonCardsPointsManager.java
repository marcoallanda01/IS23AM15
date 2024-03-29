package it.polimi.ingsw.server.model.managers;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.listeners.CommonGoalCardsListener;
import it.polimi.ingsw.server.listeners.StandardListenable;
import it.polimi.ingsw.server.model.Deck;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.Player;

import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A PointsManager that handles all the common cards, which are goals that anyone can fulfill only once,
 * this PointsManager does not take into account neither the points nor the edited bookshelf state returned from the pattern
 */
public class CommonCardsPointsManager extends CardsPointsManager implements StandardListenable {
    private final Map<Pattern, Stack<Integer>> cardsToTokens = new HashMap<>();
    private Map<Player, List<Integer>> playersToTokens = new HashMap<>();
    private Map<Player, Set<Pattern>> playersToUnfulfilledCards = new HashMap<>();

    private transient PropertyChangeSupport propertyChangeSupport;

    private Function<Integer, Stack<Integer>> generateCardTokens;
    /**
     * Generates a CommonCardsPointsManager which draws 2 cards from the given deck and assigns tokens to the cards
     * based on the number of players, it also creates an appropriate listener because some internal changes
     * (tokens on cards) need to be communicated to the view.
     * The UpdateRule of the CommonCardsPointsManager is set to END_TURN (because the order in which cards are fulfilled
     * throughout the game is relevant.
     * @param players the players
     * @param deck  the deck of patterns from which to draw 2 cards
     */
    public CommonCardsPointsManager(List<Player> players, Deck deck) {
        super(players, deck);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.updateRule = UpdateRule.END_TURN;
        this.generateCardTokens = this.defaultGenerateCardTokens();
        generatePlayersToTokens();
        generateCardsToTokens(2);
        this.propertyChangeSupport.firePropertyChange("CardsToTokensChange", null, cardsToTokens);
        generatePlayersToUnfulfilledCards();
    }
    /**
     * Generates a CommonCardsPointsManager which draws 'drawNumber' cards from the given deck and assigns tokens to the cards
     * based on the number of players, it also creates an appropriate listener because some internal changes
     * (tokens on cards) need to be communicated to the view.
     * The UpdateRule of the CommonCardsPointsManager is set to END_TURN (because the order in which cards are fulfilled
     * throughout the game is relevant.
     * @param players the players
     * @param deck  the deck of patterns from which to draw drawNumber cards
     * @param drawNumber  the number of cards to draw
     */
    public CommonCardsPointsManager(List<Player> players, Deck deck, Integer drawNumber) {
        super(players, deck);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.updateRule = UpdateRule.END_TURN;
        this.generateCardTokens = this.defaultGenerateCardTokens();
        generatePlayersToTokens();
        generateCardsToTokens(drawNumber);
        this.propertyChangeSupport.firePropertyChange("CardsToTokensChange", null, cardsToTokens);
        generatePlayersToUnfulfilledCards();
    }
    /**
     * Generates a CommonCardsPointsManager which draws 'drawNumber' cards from the given deck and assigns tokens to the cards
     * based on the number of players, using the 'generateCardTokens' function, it also creates an appropriate listener
     * because some internal changes (tokens on cards) need to be communicated to the view.
     * The UpdateRule of the CommonCardsPointsManager is set to END_TURN (because the order in which cards are fulfilled
     * throughout the game is relevant.
     * @param players the players
     * @param deck  the deck of patterns from which to draw drawNumber cards
     * @param drawNumber  the number of cards to draw
     * @param generateCardTokens a function that given the number of players returns a stack of tokens
     */
    public CommonCardsPointsManager(List<Player> players, Deck deck, Integer drawNumber, Function<Integer, Stack<Integer>> generateCardTokens) {
        super(players, deck);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.updateRule = UpdateRule.END_TURN;
        this.generateCardTokens = generateCardTokens;
        generatePlayersToTokens();
        generateCardsToTokens(drawNumber);
        this.propertyChangeSupport.firePropertyChange("CardsToTokensChange", null, cardsToTokens);
        generatePlayersToUnfulfilledCards();
    }

    /**
     * Used for deserialization
     * @param players the players
     * @param playersToPoints players to points
     * @param updateRule the update rule
     * @param deck the deck
     * @param cardsToTokens cards to tokens
     * @param playersToTokens players to tokens
     * @param playersToUnfulfilledCards players to unfulfilled cards
     */
    public CommonCardsPointsManager(List<Player> players, Map<Player, Integer> playersToPoints, UpdateRule updateRule, Deck deck, Map<Pattern, Stack<Integer>> cardsToTokens,
                                    Map<Player, List<Integer>> playersToTokens, Map<Player, Set<Pattern>> playersToUnfulfilledCards) {
        super(players, playersToPoints, updateRule, deck);
        this.cardsToTokens.putAll(cardsToTokens);
        this.playersToTokens.putAll(playersToTokens);
        this.playersToUnfulfilledCards.putAll(playersToUnfulfilledCards);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Initializes playersToTokens
     */
    private void generatePlayersToTokens() {
        playersToTokens = players.stream().collect(Collectors.toMap(player -> player, player -> new ArrayList<>()));
    }

    /**
     * Initializes cardsToTokens
     * @param drawNumber the number of cards to draw
     */
    private void generateCardsToTokens(Integer drawNumber) {
        for (int i = 0; i < drawNumber; i++) {
            Pattern card = deck.draw();
            cardsToTokens.put(card, generateCardTokens.apply(this.players.size()));
        }
    }

    /**
     * The default function to generate cards to tokens
     * @return the pile of tokens to put on a generic card, based on the number of players
     */
    private Function<Integer, Stack<Integer>>  defaultGenerateCardTokens() {
        return (playerNumber) -> {
            Stack<Integer> pile = new Stack<>();
            switch (playerNumber) {
                case 2 -> {
                    pile.push(Integer.valueOf(4));
                    pile.push(Integer.valueOf(8));
                }
                case 3 -> {
                    pile.push(Integer.valueOf(4));
                    pile.push(Integer.valueOf(6));
                    pile.push(Integer.valueOf(8));
                }
                default -> { // so 4 as well
                    pile.push(Integer.valueOf(2));
                    pile.push(Integer.valueOf(4));
                    pile.push(Integer.valueOf(6));
                    pile.push(Integer.valueOf(8));
                }
            }
            return pile;
        };
    }

    /**
     * initializes playersToUnfulfilledCards
     */
    private void generatePlayersToUnfulfilledCards() {
        playersToUnfulfilledCards = players.stream().collect(Collectors.toMap(player -> player, player -> cardsToTokens.keySet()));
    }

    /**
     * updates the points of the given player
     * @param player the player to update
     */
    public void updatePoints(Player player) {
        if (!this.canBeUpdated(player)) return;
        update(player);
        Integer newPoints = this.getPlayersToTokens().get(player).stream().reduce(0, Integer::sum);
        this.playersToPoints.put(player, newPoints);
    }

    /**
     * updates playersToUnfulfilledCards, playersToTokens, cardsToTokens,
     * based on the player's unfulfilled cards fulfilled by the player
     * @param player the player to check for cards fulfillment
     */
    private void update(Player player) {
        // partitioning the cards of each player in fulfilled and unfulfilled
        Map<Boolean, Set<Pattern>> cardsPartition = partitionCardsByFulfillment(player, this.playersToUnfulfilledCards.get(player));
        //moving the token from fulfilled cards to player
        cardsPartition.get(true).forEach((moveTokenFromCardToPlayer(player)));
        //due to moveTokenFromCardToPlayer
        this.propertyChangeSupport.firePropertyChange("CardsToTokensChange", null, cardsToTokens);
        //updating unfulfilledCards
        this.playersToUnfulfilledCards.put(player, cardsPartition.get(false));
    }

    /**
     * Partitions the old unfulfilled cards of the player in two partitions:
     * one with the cards fulfilled by the player (true)
     * one with the cards unfulfilled by the player (false)
     * @param player the player to check for cards fulfillment
     * @param cards  the unfulfilled cards of the checked player
     * @return {@literal a map (of type Map<Boolean, Set<Pattern>>) where
     * map.get(true) is a Set of cards that have just been fulfilled
     * map.get(false) is a Set of cards that have not yet been fulfilled}
     */
    private Map<Boolean, Set<Pattern>> partitionCardsByFulfillment(Player player, Set<Pattern> cards) {
        return cards.stream().collect(Collectors.partitioningBy(hasPlayerFulfilledCard(player), Collectors.toSet()));
    }

    /**
     * Given the player, returns a predicate that applies the function of the card to the bookshelf of the player
     * @param player the player to check for card fulfillment
     * @return a predicate that given a card to test returns true if
     * the player has fulfilled it and false if the player has not
     */
    private Predicate<Pattern> hasPlayerFulfilledCard(Player player) {
        return (card) -> (card.getPatternFunction().apply(player.getBookShelf().getState()) > 0);
    }

    /**
     * Given the player, returns a consumer that moves the token
     * @param player the player that takes the token
     * @return a consumer that given a card:
     * removes the token from that card (editing cardsToTokens)
     * and gives it to the player (editing playersToTokens)
     */
    private Consumer<Pattern> moveTokenFromCardToPlayer(Player player) {
        return (card) -> playersToTokens.get(player).add(cardsToTokens.get(card).pop());
    }
    /**
     * Checks if the player can be updated
     * @param player the player to check
     * @return true if the player can be updated
     */
    protected boolean canBeUpdated(Player player) {
        if(this.players.contains(player) && this.playersToTokens.containsKey(player) && this.playersToUnfulfilledCards.containsKey(player)) return true;
        return false;
    }

    /**
     * Getter for cards to tokens
     * @return cardsToTokens:
     */
    public Map<Pattern, Stack<Integer>> getCardsToTokens() {
        return new HashMap<>(cardsToTokens);
    }

    /**
     * Getter for playersToTokens
     * @return playersToTokens:
     */
    public Map<Player, List<Integer>> getPlayersToTokens() {
        return new HashMap<>(playersToTokens);
    }

    /**
     * Getter for playersToUnfulfilledCards
     * @return playersToUnfulfilledCards:
     */
    public Map<Player, Set<Pattern>> getPlayersToUnfulfilledCards() {
        return new HashMap<>(playersToUnfulfilledCards);
    }

    /**
     * Getter for the tokens of a player
     * @param player the player
     * @return the tokens of the player
     */
    public List<Integer> getTokens(Player player) {
        return new ArrayList<>(playersToTokens.get(player));
    }

    /**
     * Getter for the unfulfilledCards of a player
     * @param player the player
     * @return the unfulfilled cards of the player
     */
    public Set<Pattern> getUnfulfilledCards(Player player) {
        return new HashSet<>(playersToUnfulfilledCards.get(player));
    }

    /**
     * Getter for the fulfilledCards of a player
     * @param player the player
     * @return the fulfilled cards of the player
     */
    public Set<Pattern> getFulfilledCards(Player player) {
        Set<Pattern> playerFulfilledCards = new HashSet<>(cardsToTokens.keySet());
        playerFulfilledCards.removeAll(this.getUnfulfilledCards(player));
        return playerFulfilledCards;
    }

    /**
     * Methods that allow the registration of a standard listener. Call it only once.
     *
     * @param pushNotificationController object PushNotificationController is necessary to launch notifications to the
     *                                   clients
     */
    @Override
    public void setStandardListener(PushNotificationController pushNotificationController) {
        this.propertyChangeSupport.addPropertyChangeListener(new CommonGoalCardsListener(pushNotificationController));
    }
    /**
     * Force notification
     */
    public void notifyListeners(){
        this.propertyChangeSupport.firePropertyChange("CardsToTokensChange", null, cardsToTokens);
    }
}
