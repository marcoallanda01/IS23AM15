package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class GameSaved extends Msg{

    public String game;

    /**
     * GameSaved notification constructor
     * @param game save name
     */
    public GameSaved(@NotNull String game){
        super("GameSaved");
        this.game = game;
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
        if(!"GameSaved".equals(gs.name) || gs.game == null){
            return Optional.empty();
        }
        return Optional.of(gs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSaved gameSaved = (GameSaved) o;
        return Objects.equals(game, gameSaved.game);
    }
}
