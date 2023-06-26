package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Command to join a new game as first player
 */
public class JoinNewAsFirst extends Command{
    /**
     * player's new name
     */
    public String player;
    /**
     * size of the lobby
     */
    public int numOfPlayers;
    /**
     * id of the first player
     */
    public String idFirstPlayer;
    /**
     * easy rule game mode selector
     */
    public boolean easyRules;


    /**
     * JoinNewAsFirst constructor
     * @param name player's name
     * @param numPlayersGame size of the lobby
     * @param idFirstPlayer first player's id
     * @param easyRules true for easy rule game mode
     */
    public JoinNewAsFirst(@NotNull String name, int numPlayersGame, @NotNull String idFirstPlayer, boolean easyRules){
        super("JoinNewAsFirst");
        this.player = new String(name);
        this.numOfPlayers = numPlayersGame;
        this.idFirstPlayer = new String(idFirstPlayer);
        this.easyRules = easyRules;
    }

    /**
     * Create from json string JoinNewAsFirst object
     * @param json json string
     * @return Optional of the JoinNewAsFirst object, empty if there was a syntax error or parameters null
     */
    public static Optional<JoinNewAsFirst> fromJson(String json) {
        JoinNewAsFirst jnfp;
        try{
            Gson gson = new Gson();
            jnfp = gson.fromJson(json, JoinNewAsFirst.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"JoinNewAsFirst".equals(jnfp.name) || jnfp.player == null || jnfp.idFirstPlayer == null){
            return Optional.empty();
        }
        return Optional.of(jnfp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinNewAsFirst that = (JoinNewAsFirst) o;
        return numOfPlayers == that.numOfPlayers && easyRules == that.easyRules &&
                Objects.equals(player, that.player) && Objects.equals(idFirstPlayer, that.idFirstPlayer);
    }

}
