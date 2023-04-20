package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Hello extends Msg {
    public boolean lobbyReady;
    public String firstPlayerId;

    /**
     * Message Used to answer the hello of the client
     * @param firstPlayerId fist player's id
     */
    public Hello(@NotNull String firstPlayerId) {
        super("Hello");
        this.lobbyReady = false;
        this.firstPlayerId = firstPlayerId;
    }

    /**
     * @param lobbyReady true other players can join the lobby
     */
    public Hello(boolean lobbyReady) {
        super("hello");
        this.lobbyReady = lobbyReady;
        this.firstPlayerId = null;
    }

    public static Hello fromJson(String json) {
        try{
            Gson gson = new Gson();
            return gson.fromJson(json, Hello.class);
        }
        catch (JsonSyntaxException e){
            return null;
        }
    }
}

