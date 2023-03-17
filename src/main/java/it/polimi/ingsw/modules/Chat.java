package it.polimi.ingsw.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chat {
     private Map<Player, List<Message>> MessagesPerPlayer;
     public  void Chat(List<Player> players){
         for (Player p : players){
              MessagesPerPlayer.put(p, new ArrayList<>());
         }
     }

     //IMPORTANTE: va implementata la persistenza della chat

    /**
     * Get all the messages sent to the player p
     * @param p the player
     * @return a list of messages
     */
     public List<Message> getMessages(Player p){
         return MessagesPerPlayer.get(p);
     }

    /**
     * Add a message to the chat
     * If the message has a receiver, it will be added only to that player
     * If the message has no receiver, it will be added to all the players
     * @param m the message
     */
    public void addMessage(Message m){
         if(m.receiver.isPresent()){
             MessagesPerPlayer.get(m.receiver).add(m);
         }else{
             for (Player p : MessagesPerPlayer.keySet()){
                 MessagesPerPlayer.get(p).add(m);
             }
         }
     }
}
