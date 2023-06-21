package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.chat.Message;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private final Game game;

    /**
     * Chat controller constructor
     * @param game game
     */
    public ChatController(Game game) {
        this.game = game;
    }

    /**
     * Send a message to a player
     * @param sender sender's name
     * @param receiver receiver's name
     * @param content message's content
     */
    public synchronized void sendMessage(String sender, String receiver, String content) throws PlayerNotFoundException {
        game.sendMessage(sender, receiver, content);
    }
    /**
     * Send a message to all players
     * @param sender sender's name
     * @param content message's content
     */
    public synchronized void sendMessage(String sender, String content) throws PlayerNotFoundException {
        game.sendMessage(sender, content);
    }

    /**
     * Get player's messages
     * @param player player's name
     * @return list of all player's messages
     * @throws PlayerNotFoundException if no player wa found
     */
    public synchronized List<Message> getPlayerMessages(String player) throws PlayerNotFoundException {
        for(Player p : game.getPlayersList()){
            if(p.getUserName().equals(player)){
                return new ArrayList<>(game.getChat().getMessages(p));
            }
        }
        throw new PlayerNotFoundException("getBookshelf: no player "+player+" found");
    }
}