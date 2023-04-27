package it.polimi.ingsw.communication.responses;

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
        this.firstPlayerId = "NoFirst";
    }

    /**
     * Generator of Hello from a json string
     * @param json json string from which generate Hello
     * @return Optional of Hello, empty if json string was not coherent
     */
    public static Optional<Hello> fromJson(String json) {
        Hello h;
        try{
            Gson gson = new Gson();
            h = gson.fromJson(json, Hello.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"Hello".equals(h.name) || h.firstPlayerId == null){
            return Optional.empty();
        }
        return Optional.of(h);
    }


    /**
     * How described in the protocol first I check the firstPlayerId and only after the lobby
     * @param o Object to compare
     * @return true/false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hello hello = (Hello) o;
        if(this.firstPlayerId == null || hello.firstPlayerId == null){
            return false;
        }
        return firstPlayerId.equals(hello.firstPlayerId) && (lobbyReady == hello.lobbyReady);
    }
}

