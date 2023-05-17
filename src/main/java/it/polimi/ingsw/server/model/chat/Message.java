package it.polimi.ingsw.server.model.chat;

import it.polimi.ingsw.server.model.Player;

import java.time.LocalDateTime;
import java.util.Optional;

public class Message {
    protected final Player sender;
    protected Optional<Player> receiver;
    protected LocalDateTime date;
    protected final String content;

    /**
     * Create a new message
     * @param content the content of the message
     * @param receiver the receiver of the message
     */
    public Message(Player sender, Player receiver, String content){
        this.content = content;
        this.sender = sender;
        this.receiver = Optional.of(receiver);
        this.date = LocalDateTime.now();
    }
    /**
     * Create a new message
     * @param content the content of the message
     */
    public Message(Player sender, String content){
        this.content = content;
        this.sender = sender;
        this.receiver = Optional.empty();
        this.date = LocalDateTime.now();
    }

    /**
     * Get the content of the message
     * @return the content of the message
     */
    public String getContent(){
        return new String(content);
    }

    /**
     * Get the sender username
     * @return sender username
     */
    public String getSenderName(){
        return this.sender.getUserName();
    }

    /**
     * Get the sender
     * @return sender
     */
    public Player getSender(){
        return this.sender;
    }

    /**
     * Get the date on which the message was sent
     * @return date as a string
     */
    public String getDate(){
        return this.date.toString();
    }

    /**
     * Get username of receiver player
     * @return optional of receiver player. If empty all players are receivers.
     */
    public Optional<String> getReceiverName(){
        return this.receiver.map(Player::getUserName);
    }

}