package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.model.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BookShelfUpdate extends Msg{
    public String player;
    public Set<Tile> tiles;

    /**
     * BookShelfUpdate constructor
     * @param player player name
     * @param tiles tiles of de bookshelf
     */
    public BookShelfUpdate(@NotNull String player, @NotNull Set<Tile> tiles) {
        super("BookShelfUpdate");
        this.player = player;
        this.tiles = new HashSet<>();
        this.tiles.addAll(tiles);
    }

    /**
     * Generator of BookShelfUpdate from a json string
     * @param json json string from which generate returned object
     * @return Optional of BookShelfUpdate, empty if json string was not coherent
     */
    public static Optional<BookShelfUpdate> fromJson(String json) {
        BookShelfUpdate bsu;
        try {
            Gson gson = new Gson();
            bsu = gson.fromJson(json, BookShelfUpdate.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"BookShelfUpdate".equals(bsu.name) || bsu.tiles == null || bsu.player == null){
            return Optional.empty();
        }
        return Optional.of(bsu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookShelfUpdate that = (BookShelfUpdate) o;
        if(! Objects.equals(this.player, that.player) ) return false;
        if(this.tiles == null || that.tiles == null) return false;
        return Objects.equals(tiles, that.tiles);
    }
}
