package it.polimi.ingsw.server.model.managers.patterns;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.exceptions.InvalidPatternParameterException;

import java.util.*;
import java.util.function.Function;

/**
 * A pattern describing a personal card's objective
 */
public class PersonalPattern extends Pattern {
    private final Set<Tile> tiles;
    private final List<int[]> checkToPoints;

    /**
     * Constructor of a personal pattern given the params
     *
     * @param name          name of the pattern
     * @param tiles         tiles in the pattern, must be immutable
     * @param checkToPoints list of couple [num of corrects, points], must be immutable
     * @throws InvalidPatternParameterException if the parameters are invalid
     */
    public PersonalPattern(String name, Set<Tile> tiles, List<int[]> checkToPoints) throws InvalidPatternParameterException {
        super(name);
        this.tiles = tiles;
        this.checkToPoints = checkToPoints;
        this.checkParameters();
        this.sortCheckToPoints();
    }

    /**
     * Checks the constructor parameters
     *
     * @throws InvalidPatternParameterException if the parameters are invalid
     */
    private void checkParameters() throws InvalidPatternParameterException {
        if (this.tiles == null) {
            throw new InvalidPatternParameterException("tiles cannot be null");
        }
        for (Tile tile : tiles) {
            if (tile == null) {
                throw new InvalidPatternParameterException("tiles cannot contain a null element");
            }
        }
        if (this.checkToPoints == null) {
            throw new InvalidPatternParameterException("checkToPoints cannot be null");
        }
        for (int i = 0; i < this.checkToPoints.size(); i++) {
            if (this.checkToPoints.get(i) == null) {
                throw new InvalidPatternParameterException("checkToPoints cannot contain a null element");
            }
        }
        for (int i = 0; i < this.checkToPoints.size(); i++) {
            if (this.checkToPoints.get(i)[0] <= 0) {
                throw new InvalidPatternParameterException("checkToPoints must contain arrays with the first element (arr[0]) set to a strictly positive value");
            }
        }
        if (this.tiles.size() < this.checkToPoints.size()) {
            throw new InvalidPatternParameterException("checkToPoints must have the same size or less than tiles");
        }
    }

    /**
     * Sorts CheckToPoints so that the most points are always given
     */
    private void sortCheckToPoints() {
        Comparator<int[]> cmp = new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]); // Compare the first element in descending order
            }
        };
        Collections.sort(this.checkToPoints, cmp); // Sort the list using the custom comparator
    }

    /**
     * Returns the pattern function
     *
     * @return function that returns points you get from number of tiles in right spots
     */
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        final Set<Tile> tiles = this.tiles;
        final List<int[]> checkToPoints = this.checkToPoints;

        return (board) -> {
            List<Optional<Tile>> boardList = board.stream()
                    .flatMap(Collection::stream)
                    .toList();

            int corrects = 0;
            for (Tile tile : tiles) {
                if (boardList.contains(Optional.of(tile)))
                    corrects++;
            }

            for (int[] couple : checkToPoints) {
                if (corrects >= couple[0])
                    return couple[1];
            }
            return 0;
        };
    }
}
