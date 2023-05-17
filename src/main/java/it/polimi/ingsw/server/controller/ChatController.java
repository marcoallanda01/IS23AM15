package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;

public class ChatController {
    private final Game game;

    public ChatController(Game game) {
        this.game = game;
    }
    public synchronized void sendMessage(String sender, String receiver, String content) throws PlayerNotFoundException {
        game.sendMessage(sender, receiver, content);
    }
    public synchronized void sendMessage(String sender, String content) throws PlayerNotFoundException {
        game.sendMessage(sender, content);
    }
}
