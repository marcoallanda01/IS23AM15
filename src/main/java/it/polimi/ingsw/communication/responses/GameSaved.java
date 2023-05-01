package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

public class GameSaved extends Msg{

    public GameSaved(){
        super("GameSaved");
    }

    /**
     * Generator of GameSaved from a json string
     * @param json json string from which generate object
     * @return Optional of GameSaved, empty if json string was not coherent
     */
    public static Optional<GameSaved> fromJson(String json) {
        GameSaved gs;
        try{
            Gson gson = new Gson();
            gs = gson.fromJson(json, GameSaved.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"GameSaved".equals(gs.name)){
            return Optional.empty();
        }
        return Optional.of(gs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
