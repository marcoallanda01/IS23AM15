package it.polimi.ingsw.modules;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collection;
import java.util.function.Function;
public class Personal extends Pattern{
    List<Tile> tiles;
    List<int[]> checkToPoints;

    /**
     *
     * @param name name of the pattern
     * @param tiles tiles in the pattern
     * @param checkToPoints list of couple [num of corrects, points]. List must be in ascending order
     *                      of "num of corrects"
     */
    public Personal(String name, List<Tile> tiles, List<int[]> checkToPoints) {
        super(name);
        this.tiles = new ArrayList<Tile>(tiles);
        this.checkToPoints = new ArrayList<int[]>(checkToPoints);
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
                    if(corrects > couple[0])
                        break;
                }
                return this.checkToPoints.get(i-1)[1];
            };
    }
}
