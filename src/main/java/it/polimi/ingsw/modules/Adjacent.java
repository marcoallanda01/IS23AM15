package it.polimi.ingsw.modules;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Adjacent extends Pattern{
    public Adjacent(String name, int minTiles, int maxTiles, int points){
        super(name);
        // altro
    }

    @Override
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return null;
    }
}
