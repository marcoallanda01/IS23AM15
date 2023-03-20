package it.polimi.ingsw.modules;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class Pattern {
    /**
     * @return function that find that type of pattern
     */
    public abstract Function<List<List<Optional<Tile>>>, Integer> getPatternFunction();

}
