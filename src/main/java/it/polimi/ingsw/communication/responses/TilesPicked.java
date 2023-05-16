package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.model.TileType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TilesPicked extends Msg{
    public String player;
    public List<TileType> tiles;

    public TilesPicked(@NotNull String player, @NotNull List<TileType> tiles) {
        super("TilesPicked");
        this.player = player;
        this.tiles = new ArrayList<>(tiles);
    }

    /**
     * Generator of TilesPicked from a json string
     * @param json json string from which generate returned object
     * @return Optional of TilesPicked, empty if json string was not coherent
     */
    public static Optional<TilesPicked> fromJson(String json) {
        TilesPicked tp;
        try {
            Gson gson = new Gson();
            tp = gson.fromJson(json, TilesPicked.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"TilesPicked".equals(tp.name) || tp.tiles == null || tp.player == null){
            return Optional.empty();
        }
        return Optional.of(tp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TilesPicked that = (TilesPicked) o;
        if(! Objects.equals(this.player, that.player) ) return false;
        if(this.tiles == null || that.tiles == null) return false;
        return Objects.equals(tiles, that.tiles);
    }
}
