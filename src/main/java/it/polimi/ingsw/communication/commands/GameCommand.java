package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

public abstract class GameCommand extends Command{
    protected String id;

    /**
     * Command that perform a specific player
     * @param name command name
     * @param id player unique id
     */
    public GameCommand(String name, String id){
        super(name);
        this.id = id;
    }

    /**
     * @return id of the player that requested the command
     */
    public String getId(){
        return this.id;
    }
}
