package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Deck {
    // using List of cards because of indexes (for random drawing), might want to use other structures
    List<Pattern> cards = new ArrayList<Pattern>();

    /**
     *
     * @param patterns create a card for every pattern passed
     */
    public Deck(Set<Pattern> patterns) {
        patterns.forEach(pattern -> cards.add(pattern));
    }

    public Pattern draw() {
        int randomCardIndex = (int) Math.round(Math.random() * (cards.size() + 1));
        Pattern result = cards.get(randomCardIndex);
        cards.remove(randomCardIndex);
        return result;
    }
}
