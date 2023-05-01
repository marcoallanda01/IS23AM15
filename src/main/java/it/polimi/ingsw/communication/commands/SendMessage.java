package it.polimi.ingsw.communication.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class SendMessage extends GameCommand {

    public String message;
    public String player;

    /**
     * Send a message to a player
     * @param id id of player that send the message
     * @param message message
     * @param toPlayer recipient's name
     */
    public SendMessage(@NotNull String id, @NotNull String message, @NotNull String toPlayer){
        super("SendMessage", id);
        this.message = message;
        this.player = toPlayer;
    }

    /**
     * Send a message to all players
     * @param id id of player that send the message
     * @param message message
     */
    public SendMessage(@NotNull String id, @NotNull String message){
        super("SendMessage", id);
        this.message = message;
        this.player = null;
    }

    /**
     * Generator of SendMessage from a json string
     * @param json json string from which generate returned object
     * @return Optional of SendMessage, empty if json string was not coherent
     */
    public static Optional<SendMessage> fromJson(String json){
        SendMessage sm;
        try{
            Gson gson = new Gson();
            sm = gson.fromJson(json, SendMessage.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"SendMessage".equals(sm.name) || sm.id == null || sm.message == null){
            return Optional.empty();
        }
        return Optional.of(sm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendMessage that = (SendMessage) o;
        return Objects.equals(message, that.message) && Objects.equals(player, that.player);
    }
}
