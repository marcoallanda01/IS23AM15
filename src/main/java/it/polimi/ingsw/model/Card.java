package it.polimi.ingsw.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Card {
    private final Pattern pattern;

    /**
     *
     * @param pattern pattern that identify card
     */
    public Card(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     *
     * @return name of the card pattern
     */
    public String getName() {
        return this.pattern.getName();
    }

    // da definire
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return (bookshelfState -> 1);
    }
}
