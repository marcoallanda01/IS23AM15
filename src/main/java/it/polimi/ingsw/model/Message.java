package it.polimi.ingsw.model;

import java.time.LocalDateTime;
import java.util.Optional;

public class Message {
    public Optional<Player> receiver;
    public LocalDateTime date;
    private final String content;

    /**
     * Create a new message
     * @param content the content of the message
     * @param receiver the receiver of the message
     */
    public Message(String content, Player receiver){
        this.content = content;
        this.receiver = Optional.of(receiver);
        this.date = LocalDateTime.now();
    }
    /**
     * Create a new message
     * @param content the content of the message
     */
    public Message(String content){
        this.content = content;
        this.receiver = Optional.empty();
        this.date = LocalDateTime.now();
    }

    /**
     * Get the content of the message
     * @return the content of the message
     */
    public String getContent(){
        return content;
    }
}
