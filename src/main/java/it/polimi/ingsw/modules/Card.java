package it.polimi.ingsw.modules;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Card {
    Pattern pattern;
    String name;

    public Card(Pattern pattern, String name) {
        this.pattern = pattern;
        this.name = name;
    }

    // da definire
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return (bookshelfState -> 1);
    }
}
