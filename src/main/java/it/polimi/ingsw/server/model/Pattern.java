package it.polimi.ingsw.server.model;

import java.util.List;
import java.util.Objects;
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

    @Override
    public String toString() {
        return "Pattern{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern = (Pattern) o;
        return Objects.equals(name, pattern.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
