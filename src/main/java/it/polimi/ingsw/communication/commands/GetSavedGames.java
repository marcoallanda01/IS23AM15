package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class GetSavedGames extends Command{
    protected String idFirstPlayer;
    public GetSavedGames(@NotNull String idFistPlayer){
        super("GetSavedGames");
        this.idFirstPlayer = new String(idFistPlayer);
    }

    /**
     * Generator of GetSavedGames from a json string
     * @param json json string from which generate object
     * @return Optional of GetSavedGames, empty if json string was not coherent
     */
    public static Optional<GetSavedGames> fromJson(String json) {
        GetSavedGames gsg;
        try{
            Gson gson = new Gson();
            gsg = gson.fromJson(json, GetSavedGames.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"GetSavedGames".equals(gsg.name) || gsg.idFirstPlayer == null){
            return Optional.empty();
        }
        return Optional.of(gsg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(! (o != null && getClass() == o.getClass()) ) return false;
        GetSavedGames that = (GetSavedGames) o;
        return Objects.equals(this.idFirstPlayer, that.idFirstPlayer);
    }
}
