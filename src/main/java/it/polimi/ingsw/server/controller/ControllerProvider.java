package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.Game;

public class ControllerProvider {
    private final ChatController chatController;
    private final PlayController playController;

    public ControllerProvider(Game game, String directory, PushNotificationController pushNotificationController) {
        this.chatController = new ChatController(game);
        this.playController = new PlayController(game, directory, pushNotificationController);
    }

    public ChatController getChatController() {
        return chatController;
    }
    public PlayController getPlayController() {
        return playController;
    }
}
