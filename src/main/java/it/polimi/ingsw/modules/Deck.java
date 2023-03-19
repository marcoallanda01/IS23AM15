package it.polimi.ingsw.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Deck {
    // using List of cards because of indexes (for random drawing), might want to use other structures
    List<Card> cards = new ArrayList<>();
    public Deck(Set<Pattern> patterns) {
        // assigning default names to cards if names are not provided: C0, C1, C2...
        patterns.stream().forEach(pattern -> cards.add(new Card(pattern, "C" + cards.size())));
    }
    public Deck(Map<Pattern, String> patternsToNames) {
        patternsToNames.entrySet().stream().forEach(entry -> cards.add(new Card(entry.getKey(), entry.getValue())));
    }
    public Card draw() {
        int randomCardIndex = (int) Math.round(Math.random() * (cards.size() + 1));
        Card result = cards.get(randomCardIndex);
        cards.remove(randomCardIndex);
        return result;
    }
}
