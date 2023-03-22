package it.polimi.ingsw.modules;
import java.util.List;

public abstract class State {
    public Turn turn;

    public State(Turn turn) {
        this.turn = turn;
    }

    abstract public boolean pickTiles(List<Tile> tiles);

    abstract public  boolean putTiles(List<Tile> tiles, int column);

}
