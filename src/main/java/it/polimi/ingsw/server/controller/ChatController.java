package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.PlayerNotFoundException;

public class ChatController {
    private final Game game;

    public ChatController(Game game) {
        this.game = game;
    }
    public void sendMessage(String sender, String receiver, String content) throws PlayerNotFoundException {
        game.sendMessage(sender, receiver, content);
    }
    public void sendMessage(String sender, String content) throws PlayerNotFoundException {
        game.sendMessage(sender, content);
    }
}
