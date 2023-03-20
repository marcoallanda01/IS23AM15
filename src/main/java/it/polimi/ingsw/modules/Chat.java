package it.polimi.ingsw.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chat {
     private Map<Player, List<Message>> MessagesPerPlayer;
     public Chat(List<Player> players){
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
     * If the receiver of the message is not present, the message is sent to all the players
     * @param m the message to add
     * @throws PlayerNotFoundException if the receiver of the message is not in the game
     */
    public void addMessage(Message m) throws PlayerNotFoundException{
         if(m.receiver.isPresent()){
            if(MessagesPerPlayer.containsKey(m.receiver.get())){
                MessagesPerPlayer.get(m.receiver.get()).add(m);
            }else{
                throw new PlayerNotFoundException();
            }
         }else{
             for (Player p : MessagesPerPlayer.keySet()){
                 MessagesPerPlayer.get(p).add(m);
             }
         }
     }
}
