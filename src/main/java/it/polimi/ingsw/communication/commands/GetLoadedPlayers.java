package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

/**
 * Command to get the players of a loaded game
 */
public class GetLoadedPlayers extends Command{

    /**
     * GetLoadedPlayers constructor
     */
    public GetLoadedPlayers(){super("GetLoadedPlayers");}

    /**
     * Generator of GetLoadedPlayers from a json string
     * @param json json string from which generate object
     * @return Optional of GetLoadedPlayers, empty if json string was not coherent
     */
    public static Optional<GetLoadedPlayers> fromJson(String json) {
        GetLoadedPlayers glp;
        try{
            Gson gson = new Gson();
            glp = gson.fromJson(json, GetLoadedPlayers.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"GetLoadedPlayers".equals(glp.name)){
            return Optional.empty();
        }
        return Optional.of(glp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
