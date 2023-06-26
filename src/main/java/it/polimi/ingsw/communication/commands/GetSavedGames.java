package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Command to get the saves
 */
public class GetSavedGames extends Command{

    /**
     * GetSavedGames constructor
     */
    public GetSavedGames(){
        super("GetSavedGames");
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
        if(!"GetSavedGames".equals(gsg.name)){
            return Optional.empty();
        }
        return Optional.of(gsg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
