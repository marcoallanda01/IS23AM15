package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.function.Function;
public class PersonalPattern extends Pattern{
    private final List<Tile> tiles;
    private final List<int[]> checkToPoints;

    /**
     * @param name name of the pattern
     * @param tiles tiles in the pattern, must be immutable
     * @param checkToPoints list of couple [num of corrects, points], must be immutable
     */
    public PersonalPattern(String name, List<Tile> tiles, List<int[]> checkToPoints) throws InvalidPatternParameterException {
        super(name);
        this.tiles = tiles;
        this.checkToPoints = checkToPoints;
        this.checkParameters();
        this.sortCheckToPoints();
    }
    /**
     * Checks the constructor parameters
     * @throws InvalidPatternParameterException if the parameters are invalid
     */
    private void checkParameters()  throws InvalidPatternParameterException{
        if (this.tiles == null) {
            throw new InvalidPatternParameterException("tiles cannot be null");
        }
        for (int i = 0; i< this.tiles.size(); i++) {
            if (this.tiles.get(i) == null) {
                throw new InvalidPatternParameterException("tiles cannot contain a null element");
            }
        }
        if (this.checkToPoints == null) {
            throw new InvalidPatternParameterException("checkToPoints cannot be null");
        }
        for (int i = 0; i< this.checkToPoints.size(); i++) {
            if (this.checkToPoints.get(i) == null) {
                throw new InvalidPatternParameterException("checkToPoints cannot contain a null element");
            }
        }
        for (int i = 0; i< this.checkToPoints.size(); i++) {
            if (this.checkToPoints.get(i)[0] <= 0) {
                throw new InvalidPatternParameterException("checkToPoints must contain arrays with the first element (arr[0]) set to a strictly positive value");
            }
        }
        if (this.tiles.size() < this.checkToPoints.size()) {
            throw new InvalidPatternParameterException("checkToPoints must have the same size or less than tiles");
        }
    }
    private void sortCheckToPoints() {
        Comparator<int[]> cmp = new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]); // Compare the first element in descending order
            }
        };
        Collections.sort(this.checkToPoints, cmp); // Sort the list using the custom comparator
    }

    /**
     *
     * @return function that returns points you get from number of tiles in right spots
     */
    @java.lang.Override
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return (board) -> {
                List<Optional<Tile>> boardList = board.stream()
                        .flatMap(Collection::stream)
                        .toList();

                Integer corrects = 0;
                for (Tile tile : tiles) {
                    if (boardList.contains(Optional.of(tile)))
                        corrects++;
                }
                int i;
                for(i = 0; i < this.checkToPoints.size(); i++){
                    int[] couple = this.checkToPoints.get(i);
                    if(corrects >= couple[0])
                        return couple[1];
                }
                return 0;
            };
    }
}
