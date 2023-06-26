package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Command to load a saved game
 */
public class LoadGame extends Command{
    /**
     * id of the first player
     */
    public String idFirstPlayer;
    /**
     * save name
     */
    public String game;

    /**
     * LoadGame constructor
     * @param idFistPlayer first player's id
     * @param game game name to load
     */
    public LoadGame(@NotNull String idFistPlayer, @NotNull String game){
        super("LoadGame");
        this.idFirstPlayer = new String(idFistPlayer);
        this.game = game;
    }

    /**
     * Generator of LoadGame from a json string
     * @param json json string from which generate object
     * @return Optional of LoadGame, empty if json string was not coherent
     */
    public static Optional<LoadGame> fromJson(String json) {
        LoadGame lg;
        try{
            Gson gson = new Gson();
            lg = gson.fromJson(json, LoadGame.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"LoadGame".equals(lg.name) || lg.idFirstPlayer == null || lg.game == null){
            return Optional.empty();
        }
        return Optional.of(lg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(! (o != null && getClass() == o.getClass()) ) return false;
        LoadGame that = (LoadGame) o;
        return Objects.equals(this.idFirstPlayer, that.idFirstPlayer) &&
                Objects.equals(this.game, that.game);
    }
}
