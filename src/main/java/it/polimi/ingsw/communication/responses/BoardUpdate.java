package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Msg that identifies a change in the LivingRoomBoard
 */
public class BoardUpdate extends Msg {
    /**
     * LivingRoomBoard tiles
     */
    public Set<Tile> tiles;

    /**
     * BoardUpdate constructor
     * @param tiles tiles of de board
     */
    public BoardUpdate(@NotNull Set<Tile> tiles) {
        super("BoardUpdate");
        this.tiles = new HashSet<>();
        this.tiles.addAll(tiles);
    }

    /**
     * Generator of BoardUpdate from a json string
     * @param json json string from which generate returned object
     * @return Optional of BoardUpdate, empty if json string was not coherent
     */
    public static Optional<BoardUpdate> fromJson(String json) {
        BoardUpdate bu;
        try {
            Gson gson = new Gson();
            bu = gson.fromJson(json, BoardUpdate.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"BoardUpdate".equals(bu.name) || bu.tiles == null){
            return Optional.empty();
        }
        return Optional.of(bu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardUpdate that = (BoardUpdate) o;
        if(this.tiles == null || that.tiles == null) return false;
        return Objects.equals(tiles, that.tiles);
    }
}
