package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;

public class ControllerProvider {
    private final ChatController chatController;
    private final PlayController playController;
    public ControllerProvider(Game game) {
        this.chatController = new ChatController(game);
        this.playController = new PlayController(game, "saves");
    }
    public ChatController getChatController() {
        return chatController;
    }
    public PlayController getPlayController() {
        return playController;
    }
}
