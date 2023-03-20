package it.polimi.ingsw.modules;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Collections;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;;
public class Personal extends Pattern{
    List<Tile> tiles = null;

    public Personal(List<Tile> tiles) {
        this.tiles = new ArrayList<Tile>(tiles);
    }

    /**
     *
     * @return function that returns number of tiles in the correct position
     */
    @java.lang.Override
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return new Function<List<List<Optional<Tile>>>, Integer>() {
            @Override
            public Integer apply(List<List<Optional<Tile>>> board){

                List<Optional<Tile>> boardList = board.stream()
                        .flatMap(Collection::stream)
                        .toList();
                Integer corrects = 0;
                for (Tile tile : tiles) {
                    if (boardList.contains(Optional.of(tile)))
                        corrects++;
                }
                return corrects;
            }
        };
    }
}
