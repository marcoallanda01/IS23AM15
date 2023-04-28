package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class LoadGame extends Command{
    protected String idFirstPlayer;
    public LoadGame(@NotNull String idFistPlayer){
        super("GetSavedGames");
        this.idFirstPlayer = new String(idFistPlayer);
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
        if(!"LoadGame".equals(lg.name) || lg.idFirstPlayer == null){
            return Optional.empty();
        }
        return Optional.of(lg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(! (o != null && getClass() == o.getClass()) ) return false;
        LoadGame that = (LoadGame) o;
        return Objects.equals(this.idFirstPlayer, that.idFirstPlayer);
    }
}
