package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.model.Game;

public class ControllerProvider {
    private final ChatController chatController;
    private final PlayController playController;

    /**
     * Creates a container of ChatController and PlayController of a Game.
     * @param game game
     * @param directory saves directory
     * @param pushNotificationController pushNotificationController
     */
    public ControllerProvider(Game game, String directory, PushNotificationController pushNotificationController) {
        this.chatController = new ChatController(game);
        this.playController = new PlayController(game, directory, pushNotificationController);
    }

    /**
     * Get chat controller
     * @return chatController of the game
     */
    public ChatController getChatController() {
        return chatController;
    }

    /**
     * Get play controller
     * @return playController of the game
     */
    public PlayController getPlayController() {
        return playController;
    }
}
