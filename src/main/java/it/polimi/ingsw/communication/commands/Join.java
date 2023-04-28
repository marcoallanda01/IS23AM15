package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

public class Join extends Command{
    public Join(){super("Join");}

    /**
     * Generator of Join from a json string
     * @param json json string from which generate object
     * @return Optional of Join, empty if json string was not coherent
     */
    public static Optional<Join> fromJson(String json) {
        Join j;
        try{
            Gson gson = new Gson();
            j = gson.fromJson(json, Join.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Join".equals(j.name)){
            return Optional.empty();
        }
        return Optional.of(j);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
