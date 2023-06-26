package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

/**
 * Command performed to see the lobby status
 */
public class HelloCommand extends Command{

    /**
     * HelloCommand constructor
     */
    public HelloCommand(){super("HelloCommand");}

    /**
     * Generator of HelloCommand from a json string
     * @param json json string from which generate object
     * @return Optional of HelloCommand, empty if json string was not coherent
     */
    public static Optional<HelloCommand> fromJson(String json) {
        HelloCommand hc;
        try{
            Gson gson = new Gson();
            hc = gson.fromJson(json, HelloCommand.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"HelloCommand".equals(hc.name)){
            return Optional.empty();
        }
        return Optional.of(hc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

}
