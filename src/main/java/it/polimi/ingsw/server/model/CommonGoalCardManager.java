package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommonGoalCardManager extends CardsAndPointsManager {
    private final Map<Card, Stack<Token>> cardsToTokens = new HashMap<>();
    private Map<Player, Set<Token>> playersToTokens = new HashMap<>();
    private Map<Player, Set<Card>> playersToUnfulfilledCards = new HashMap<>();

    public CommonGoalCardManager(List<Player> players, Deck deck) {
        super(players, deck);
        this.updateRule = UpdateRule.END_TURN;
        generatePlayersToTokens();
        generateCardsToTokens();
        generatePlayersToUnfulfilledCards();
    }

    /**
     * Used for deserialization
     */
    public CommonGoalCardManager(List<Player> players, Map<Player, Integer> playersToPoints, UpdateRule updateRule, Deck deck, Map<Card, Stack<Token>> cardsToTokens,
                                 Map<Player, Set<Token>> playersToTokens, Map<Player, Set<Card>> playersToUnfulfilledCards) {
        super(players, playersToPoints, updateRule, deck);
        this.cardsToTokens.putAll(cardsToTokens);
        this.playersToTokens.putAll(playersToTokens);
        this.playersToUnfulfilledCards.putAll(playersToUnfulfilledCards);
    }

    /**
     * Initializes playersToTokens
     */
    private void generatePlayersToTokens() {
        playersToTokens = players.stream().collect(Collectors.toMap(player -> player, player -> new HashSet<>()));
    }

    /**
     * Initializes cardsToTokens
     */
    private void generateCardsToTokens() {
        // initializing 2 common goal cards, maybe should be parametrized
        for (int i = 0; i < 2; i++) {
            Card card = deck.draw();
            cardsToTokens.put(card, generateCardTokens());
        }
    }

    /**
     * @return the pile of tokens to put on a generic card, based on the number of players
     */
    // good for now, might want to read these from json and pass it to the constructor
    private Stack<Token> generateCardTokens() {
        Stack<Token> pile = new Stack<>();
        switch (this.players.size()) {
            case 2: {
                pile.push(new Token(4, 4));
                pile.push(new Token(8, 8));
                break;
            }
            case 3: {
                pile.push(new Token(4, 4));
                pile.push(new Token(6, 6));
                pile.push(new Token(8, 8));
                break;
            }
            case 4: {
                pile.push(new Token(2, 2));
                pile.push(new Token(4, 4));
                pile.push(new Token(6, 6));
                pile.push(new Token(8, 8));
                break;
            }
            default: {
                pile.push(new Token(2, 2));
                pile.push(new Token(4, 4));
                pile.push(new Token(6, 6));
                pile.push(new Token(8, 8));
            }
        }
        return pile;
    }

    /**
     * initializes playersToUnfulfilledCards
     */
    private void generatePlayersToUnfulfilledCards() {
        playersToUnfulfilledCards = players.stream().collect(Collectors.toMap(player -> player, player -> cardsToTokens.keySet()));
    }

    /**
     * updates the points of the given player
     *
     * @param player the player to update
     */
    public void updatePoints(Player player) {
        update(player);
        Integer newPoints = this.getPlayersToTokens().get(player).stream().map(Token::getPoints).reduce(0, Integer::sum);
        this.playersToPoints.put(player, newPoints);
    }

    /**
     * updates playersToUnfulfilledCards, playersToTokens, cardsToTokens,
     * based on the player's unfulfilled cards fulfilled by the player
     *
     * @param player the player to check for cards fulfillment
     */
    private void update(Player player) {
        // partitioning the cards of each player in fulfilled and unfulfilled
        Map<Boolean, Set<Card>> cardsPartition = partitionCardsByFulfillment(player, this.playersToUnfulfilledCards.get(player));
        //moving the token from fulfilled cards to player
        cardsPartition.get(true).forEach((moveTokenFromCardToPlayer(player)));
        //updating unfulfilledCards
        this.playersToUnfulfilledCards.put(player, cardsPartition.get(false));
    }

    /**
     * partitions the old unfulfilled cards of the player in two partitions:
     * one with the cards fulfilled by the player (true)
     * one with the cards unfulfilled by the player (false)
     *
     * @param player the player to check for cards fulfillment
     * @param cards  the unfulfilled cards of the checked player
     * @return a map (of type Map<Boolean, Set<Card>>) where
     * map.get(true) is a Set of cards that have just been fulfilled
     * map.get(false) is a Set of cards that have not yet been fulfilled
     */
    private Map<Boolean, Set<Card>> partitionCardsByFulfillment(Player player, Set<Card> cards) {
        return cards.stream().collect(Collectors.partitioningBy(hasPlayerFulfilledCard(player), Collectors.toSet()));
    }

    /**
     * @param player the player to check for card fulfillment
     * @return a predicate that given a card to test returns true if
     * the player has fulfilled it and false if the player has not
     */
    private Predicate<Card> hasPlayerFulfilledCard(Player player) {
        return (card) -> (card.getPatternFunction().apply(player.getBookShelf().getState()) > 0);
    }

    /**
     * @param player the player that takes the token
     * @return a consumer that given a card:
     * removes the token from that card (editing cardsToTokens)
     * and gives it to the player (editing playersToTokens)
     */
    private Consumer<Card> moveTokenFromCardToPlayer(Player player) {
        return (card) -> playersToTokens.get(player).add(cardsToTokens.get(card).pop());
    }

    // good for now, might want to clone and/or send a simplified version of these objects for security reasons

    /**
     * @return cardsToTokens:
     */
    public Map<Card, Stack<Token>> getCardsToTokens() {
        return cardsToTokens;
    }

    /**
     * @return playersToTokens:
     */
    public Map<Player, Set<Token>> getPlayersToTokens() {
        return playersToTokens;
    }

    /**
     * @return playersToUnfulfilledCards:
     */
    public Map<Player, Set<Card>> getPlayersToUnfulfilledCards() {
        return playersToUnfulfilledCards;
    }

    /**
     * @param player the player
     * @return the tokens of the player
     */
    public Set<Token> getTokens(Player player) {
        return playersToTokens.get(player);
    }

    /**
     * @param player the player
     * @return the unfulfilled cards of the player
     */
    public Set<Card> getUnfulfilledCards(Player player) {
        return playersToUnfulfilledCards.get(player);
    }

    /**
     * @param player the player
     * @return the fulfilled cards of the player
     */
    public Set<Card> getFulfilledCards(Player player) {
        // new HashSet because we don't want to alter the map
        Set<Card> playerFulfilledCards = new HashSet<>(cardsToTokens.keySet());
        // removing the unfulfilled cards
        playerFulfilledCards.removeAll(this.getUnfulfilledCards(player));
        return playerFulfilledCards;
    }
}
