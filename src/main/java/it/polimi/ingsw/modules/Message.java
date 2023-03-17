package it.polimi.ingsw.modules;

import java.time.LocalDateTime;
import java.util.Optional;

public class Message {
    public Optional<Player> receiver;
    public LocalDateTime date;
    private String content;

    /**
     * Create a new message
     * @param content the content of the message
     * @param receiver the receiver of the message
     */
    public Message(String content, Optional<Player> receiver){
        this.content = content;
        this.receiver = receiver;
        this.date = LocalDateTime.now();
    }
    /**
     * Create a new message
     * @param content the content of the message
     */
    public Message(String content){
        this(content, Optional.empty());
    }

    /**
     * Get the content of the message
     * @return the content of the message
     */
    public String getContent(){
        return content;
    }
}
