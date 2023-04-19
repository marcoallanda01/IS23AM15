package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Hello extends Msg {
    public boolean lobbyReady;
    public Optional<String> firstPlayerId;

    /**
     * Message Used to answer the hello of the client
     * @param lobbyReady true other players can join the lobby
     * @param firstPlayerId fist player's id
     */
    public Hello(boolean lobbyReady, @NotNull String firstPlayerId) {
        super("Hello");
        this.lobbyReady = lobbyReady;
        this.firstPlayerId = Optional.of(firstPlayerId);
    }

    /**
     * @param lobbyReady true other players can join the lobby
     */
    public Hello(boolean lobbyReady) {
        super("hello");
        this.lobbyReady = lobbyReady;
        this.firstPlayerId = Optional.empty();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"lobbyReady\":");
        sb.append(this.lobbyReady);
        if (this.firstPlayerId.isPresent()) {
            sb.append(",");
            sb.append("\"firstPlayerId\":\"");
            sb.append(this.firstPlayerId.get());
            sb.append("\"");
        }
        sb.append("}");
        return toMsgJson(sb.toString());
    }

    public Hello fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Hello.class);
    }
}

