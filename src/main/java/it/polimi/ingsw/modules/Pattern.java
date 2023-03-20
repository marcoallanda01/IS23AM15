package it.polimi.ingsw.modules;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class Pattern {

    private final String name;

    /**
     *
     * @param name name of the pattern
     */
    public Pattern(String name) {
        this.name = name;
    }

    /**
     *
     * @return function to calculate points got from pattern
     */
    public abstract Function<List<List<Optional<Tile>>>, Integer> getPatternFunction();

    public String getName() {
        return name;
    }

}
