package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;


public class Deck {
    // using List of cards because of indexes (for random drawing), might want to use other structures
    List<Card> cards = new ArrayList<Card>();

    /**
     *
     * @param patterns create a card for every pattern passed
     */
    public Deck(List<Pattern> patterns) {
        patterns.forEach(pattern -> cards.add(new Card(pattern)));
    }

    public Card draw() {
        int randomCardIndex = (int) Math.round(Math.random() * (cards.size() + 1));
        Card result = cards.get(randomCardIndex);
        cards.remove(randomCardIndex);
        return result;
    }
}
