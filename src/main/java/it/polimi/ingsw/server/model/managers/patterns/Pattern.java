package it.polimi.ingsw.server.model.managers.patterns;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.exceptions.InvalidPatternParameterException;

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
    public Pattern(String name){
        this.name = name;
    }

    /**
     * @return {@literal function to calculate points got from pattern checking on the bookshelf
     * this function is allowed to EDIT the given bookshelf (List<List<Optional<Tile>>>)}
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
