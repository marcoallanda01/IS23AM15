package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * GameSetUP is the object sent to the player at the beginning of a starting game.
 * It doesn't include player points and tiles in the board/bookshelf because there are notifyChangeBoard,
 * notifyChangeBookShelf, updatePlayerPoints, notifyYourTurn, ...
 */
public class GameSetUp extends Msg{
    public List<String> players;
    public List<String> goals;
    public String personal;


    public GameSetUp(@NotNull List<String> players, @NotNull List<String> goals, @NotNull String personal){
        super("GameSetUp");
        this.players = new ArrayList<>(players);
        this.goals = new ArrayList<>(goals);
        this.personal = new String(personal);
    }


    public static Optional<GameSetUp> fromJson(String json) {
        GameSetUp gsu;
        try {
            Gson gson = new Gson();
            gsu = gson.fromJson(json, GameSetUp.class);
        }catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"GameSetUp".equals(gsu.name) || gsu.players == null || gsu.goals == null || gsu.personal == null){
            return Optional.empty();
        }
        return Optional.of(gsu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSetUp gameSetUp = (GameSetUp) o;
        return Objects.equals(players, gameSetUp.players) && Objects.equals(goals, gameSetUp.goals)
                && Objects.equals(personal, gameSetUp.personal);
    }
}
