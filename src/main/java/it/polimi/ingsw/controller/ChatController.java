package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class ChatController {
    private final Game game;

    public ChatController(Game game) {
        this.game = game;
    }
    public void sendMessage(Player sender, Player receiver, String content) {
        game.sendMessage(sender, receiver, content);
    }
    public void sendMessage(Player sender, String content) {
        game.sendMessage(sender, content);
    }
}
