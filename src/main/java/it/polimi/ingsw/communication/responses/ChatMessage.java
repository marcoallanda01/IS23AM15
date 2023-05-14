package it.polimi.ingsw.communication.responses;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.server.model.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;


/**
 *  The Chat message doesn't contain receiver because it's used buy the communication layer, there is no
 *  interest in saying: "This is message is for you, I send it only to you, you are the receiver"
 */
public class ChatMessage extends Msg{
    public String message;
    public String sender;
    public String date;

    /**
     * Constructor of ChatMessage
     * @param m Message from which generate it
     */
    public ChatMessage(@NotNull Message m){
        super("ChatMessage");
        this.message = m.getContent();
        this.sender = m.getSenderName();
        this.date = m.getDate();
    }

    /**
     * Constructor of ChatMessage
     * @param sender sender's name
     * @param date date of message creation
     * @param message actual message to be sent
     */
    public ChatMessage(String sender, String date, String message) {
        super("ChatMessage");
        this.message = message;
        this.sender = sender;
        this.date = date;
    }

    /**
     * Generator of ChatMessage from a json string
     * @param json json string from which generate returned object
     * @return Optional of ChatMessage, empty if json string was not coherent
     */
    public static Optional<ChatMessage> fromJson(@NotNull String json){
        ChatMessage cm;
        try{
            Gson gson = new Gson();
            cm = gson.fromJson(json, ChatMessage.class);
        }
        catch (JsonSyntaxException e){
            return Optional.empty();
        }
        if(!"ChatMessage".equals(cm.name) || cm.message == null ||cm.sender == null || cm.date == null){
            return Optional.empty();
        }
        return Optional.of(cm);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(message, that.message) && Objects.equals(sender, that.sender) &&
                Objects.equals(date, that.date);
    }
}
