package it.polimi.ingsw.modules;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Card {

    // da definire
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return (bookshelfState -> 1);
    }
}
