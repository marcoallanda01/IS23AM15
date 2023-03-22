package it.polimi.ingsw.modules;

import java.util.List;

public class EndState extends State{
    public EndState(Turn turn) {
        super(turn);
    }

    @Override
    public boolean pickTiles(List<Tile> tiles) {
        return false;
    }

    @Override
    public boolean putTiles(List<Tile> tiles, int column) {
        return false;
    }
}
