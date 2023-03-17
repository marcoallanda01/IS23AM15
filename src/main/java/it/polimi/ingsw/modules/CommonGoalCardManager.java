package it.polimi.ingsw.modules;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommonGoalCardManager extends  CardsAndPointsManager{
    private Map<Card, Stack<Token>> cardsToTokens  = new HashMap<>();;
    private Map<Player, Set<Token>> playersToTokens  = new HashMap<>();;
    private Map<Player, Set<Card>> playersToUnfulfilledCards = new HashMap<>();

    public CommonGoalCardManager(List<Player> players, Deck deck) {
        super(players, deck);
        generatePlayersToTokens();
        generateCardsToTokens();
        generateUnfulfilledCards();
    }
    private void generatePlayersToTokens() {
        playersToTokens = players.stream().collect(Collectors.toMap(player -> player, player -> new HashSet()));
    }
    private void generateCardsToTokens() {
        // inizializzo 2 common goal card, forse andrebbe parametrizzato
        for(int i = 0; i < 2; i++) {
            Card card = deck.Draw();
            cardsToTokens.put(card, generateCardTokens());
        }
    }
    // good for now, might want to read these from json and pass it to the constructor
    private Stack<Token> generateCardTokens() {
        Stack<Token> pile = new Stack<>();
        switch (this.players.size()) {
            case 2:
            {
                pile.push(new Token(4, 4));
                pile.push(new Token(8, 8));
                break;
            }
            case 3:
            {
                pile.push(new Token(4, 4));
                pile.push(new Token(6, 6));
                pile.push(new Token(8, 8));
                break;
            }
            case 4:
            {
                pile.push(new Token(2, 2));
                pile.push(new Token(4, 4));
                pile.push(new Token(6, 6));
                pile.push(new Token(8, 8));
                break;
            }
            default:
            {
                pile.push(new Token(2, 2));
                pile.push(new Token(4, 4));
                pile.push(new Token(6, 6));
                pile.push(new Token(8, 8));
            }
        }
        return pile;
    }
    private void generateUnfulfilledCards() {
        playersToUnfulfilledCards = players.stream().collect(Collectors.toMap(player -> player, player -> cardsToTokens.keySet()));
    }
    public void updatePoints() {
        players.stream().forEach(player -> this.updatePlayerPoints(player));
    }
    public void updatePlayerPoints(Player player) {
        player.addPoints(this.playersToTokens.get(player).stream().map(token -> token.getPoints()).reduce(0, Integer::sum));
        // if we update te player point we also need to remove tokens from player, to avoid adding them twice
        this.playersToTokens.put(player, new HashSet<>());
    }
    public void update() {
        // updates every player, even if it is not their turn, more extensible, less efficient
        players.stream().forEach(player -> this.updatePlayer(player));
    }
    public void updatePlayer(Player player) {
        // partitioning the cards of each player in fulfilled and unfulfilled
        Map<Boolean,Set<Card>> cardsPartition = partitionCardsByFulfillment(player, this.playersToUnfulfilledCards.get(player));
        //moving the token from fulfilled cards to player
        cardsPartition.get(true).stream().forEach((moveTokenFromCardToPlayer(player)));
        //updating unfulfilledCards
        this.playersToUnfulfilledCards.put(player, cardsPartition.get(false));
    }
    private Map<Boolean,Set<Card>> partitionCardsByFulfillment(Player player, Set<Card> cards) {

        return cards.stream().collect(Collectors.partitioningBy(hasPlayerFulfilledCard(player), Collectors.toSet()));
    }
    private Predicate<Card> hasPlayerFulfilledCard(Player player) {
        return (card) -> (card.getPatternFunction().apply(player.getBookShelf().getState()) > 0);
    }
    private Consumer<Card> moveTokenFromCardToPlayer(Player player) {
        return (card) -> {playersToTokens.get(player).add(cardsToTokens.get(card).pop());};
    }

    // good for now, might want to clone or send a simplified version of these objects for security reasons
    public Map<Card, Stack<Token>> getCardsToTokens() {
        return cardsToTokens;
    }
    public Map<Player, Set<Token>> getPlayersToTokens() {
        return playersToTokens;
    }
    public Map<Player, Set<Card>> getPlayersToUnfulfilledCards() {
        return playersToUnfulfilledCards;
    }
}
