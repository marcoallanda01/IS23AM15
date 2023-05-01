package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Objects;
import java.util.Optional;

public class SaveGame extends GameCommand{

    public SaveGame(String id){
        super("SaveGame", id);
    }

    /**
     * Generator of SaveGame from a json string
     * @param json json string from which generate object
     * @return Optional of SaveGame, empty if json string was not coherent
     */
    public static Optional<SaveGame> fromJson(String json) {
        SaveGame sg;
        try{
            Gson gson = new Gson();
            sg = gson.fromJson(json, SaveGame.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"SaveGame".equals(sg.name) || sg.id == null){
            return Optional.empty();
        }
        return Optional.of(sg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(! (o != null && getClass() == o.getClass()) ) return false;
        SaveGame that = (SaveGame) o;
        return Objects.equals(this.id, that.id);
    }
}

