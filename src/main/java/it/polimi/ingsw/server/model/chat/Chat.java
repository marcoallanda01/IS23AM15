package it.polimi.ingsw.server.model.chat;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.listeners.ChatListener;
import it.polimi.ingsw.server.listeners.StandardListenable;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.controller.serialization.PostProcessable;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat implements PostProcessable, StandardListenable {
     private final Map<String, List<Message>> MessagesPerPlayer;
    private transient PropertyChangeSupport propertyChangeSupport;

    /**
     * Constructor
     * @param players the players in the game
     */
     public Chat(List<Player> players){
         MessagesPerPlayer = new HashMap<>();
         for (Player p : players){
              MessagesPerPlayer.put(p.getUserName(), new ArrayList<>());
         }
         this.propertyChangeSupport = new PropertyChangeSupport(this);
     }

    /**
     * Get all the messages sent to the player p
     * @param p the player
     * @return a list of messages
     */
     public List<Message> getMessages(Player p){
         return MessagesPerPlayer.get(p.getUserName());
     }

    /**
     * Add a message to the chat
     * If the receiver of the message is not present, the message is sent to all the players
     * @param m the message to add
     * @throws PlayerNotFoundException if the receiver of the message is not in the game
     */
    public void addMessage(Message m) throws PlayerNotFoundException{
        if(MessagesPerPlayer.containsKey(m.sender.getUserName())){
            if (m.receiver.isPresent()) {
                if (MessagesPerPlayer.containsKey(m.receiver.get().getUserName())) {
                    MessagesPerPlayer.get(m.receiver.get().getUserName()).add(0,m);
                    MessagesPerPlayer.get(m.sender.getUserName()).add(0,m);
                } else {
                    throw new PlayerNotFoundException("The receiver of the message is not in the game");
                }
            } else {
                for (String p : MessagesPerPlayer.keySet()) {
                    MessagesPerPlayer.get(p).add(0, m);
                }
            }
            this.propertyChangeSupport.firePropertyChange("messageSent", null, m);
        } else {
            throw new PlayerNotFoundException("The sender of the message is not in the game");
        }
    }

    @Override
    public void gsonPostProcess() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Set standard player listener
     */
    public void setStandardListener(PushNotificationController pnc){
        this.propertyChangeSupport.addPropertyChangeListener(new ChatListener(pnc));
    }
}
