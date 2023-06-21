package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.managers.patterns.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Deck {
    // using List of cards because of indexes (for random drawing)
    List<Pattern> cards = new ArrayList<>();

    /**
     *
     * @param patterns create a card for every pattern passed
     */
    public Deck(Set<Pattern> patterns) {
        cards.addAll(patterns);
    }
    /**
     *
     * draws a card from the deck
     * @return the drawn card
     */
    public Pattern draw() {
        int randomCardIndex = new Random().nextInt(cards.size());
        Pattern result = cards.get(randomCardIndex);
        cards.remove(randomCardIndex);
        return result;
    }
}
