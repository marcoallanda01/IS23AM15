package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.View;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GUI extends View {
    private final GUIApplication guiApplication;
    private GUIState guiState;

    /**
     * Constructor for GUI
     * @param guiApplication the guiApplication to use
     */
    public GUI(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
        initializeViewAndConnection(5);
    }
    /**
     * Shows an error
     */
    @Override
    public void showError(String error) {
        Platform.runLater(() -> guiApplication.showPopup(error));
    }
    /**
     * Shows the chat
     */
    @Override
    public void showChat() {
        new GUIChat(guiApplication, this.getChat(), this.getPlayers());
    }
    /**
     * Does not do anything because help is not needed
     */
    @Override
    public void showHelp() {
        // help in our GUI implementation is not needed as it is really intuitive
    }

    /**
     * initializes the view and the connection, if it fails it will retry after a countdown that is doubled each time
     * @param countdown the countdown
     */
    private void initializeViewAndConnection(int countdown) {
        try {
            Client.getInstance().init(this);
            start();
        } catch (Exception e) {
            AtomicInteger retryCount = new AtomicInteger(countdown); // Initial retry count
            Text text = new Text("MyShelfie encountered an error while starting, will retry in: " + retryCount.get());
            text.setFont(Font.font("Arial", 18));

            // Create the layout pane
            StackPane root = new StackPane();
            root.getChildren().add(text);

            // Create the scene and set the root pane
            Scene scene = new Scene(root, 800, 700);

            // Set CSS styling for the text
            text.getStyleClass().add("my-shelfie-text");

            // Set the scene to the primary stage
            guiApplication.transitionToScene(scene);
            // Create a Timeline animation to update the retry countdown
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
                int count = retryCount.decrementAndGet();
                text.setText("MyShelfie encountered an error while starting, will retry in: " + count);
                if (count == 0) {
                    initializeViewAndConnection(countdown * 2);
                    timeline.stop();
                }
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(retryCount.get());
            timeline.play();
        }
    }
    /**
     * Logs that the gui started
     */
    public void start() {
        Client.getInstance().getLogger().log("GUI Started");
    }
    /**
     * Stops the guiApplication with a message
     * @param message the message to be shown
     */
    public void stop(String message) {
        Platform.runLater(() -> {
            try {
                guiApplication.stop(message);
            } catch (Exception e) {
                Client.getInstance().getLogger().log(e);
            }
        });
        Client.getInstance().getLogger().log("GUI Stopped");
    }
    /**
     * Shows the goals
     */
    public void showGoals() {
        Platform.runLater(() -> guiApplication.printGoals(this.getCommonCards().keySet().stream().map(goal -> goalsToDetails.get(goal)).collect(Collectors.toList()), this.getCommonGoals().stream().map(goal -> goalsToDetails.get(goal)).collect(Collectors.toList()), goalsToDetails.get(this.getPersonalGoal())));
    }
    /**
     * Renders the view
     */
    public void render(){
        Client.getInstance().getLogger().log("Rendering: " + Client.getInstance().getClientState());
        switch (Client.getInstance().getClientState()) {
            case LOGIN -> this.guiState = new GUILogin(guiApplication);
            case CREATE_LOBBY -> this.guiState = new GUICreateLobby(guiApplication, Client.getInstance().getNickname());
            case LOBBY -> this.guiState = new GUILobby(guiApplication, this.isEasyRules());
            case CREATE_GAME -> this.guiState = new GUICreateGame(guiApplication);
            case LOAD_GAME -> this.guiState = new GUILoadGame(guiApplication, this.getSavedGames());
            case LOAD_NAMES -> this.guiState = new GUILoadNames(guiApplication, this.getLobbyPlayers());
            case IN_GAME -> this.guiState = new GUIInGame(guiApplication, this.getLivingRoomBoard(), this.getBookShelves());
            case END_GAME -> this.guiState = new GUIEndGame(guiApplication, this.getPoints(), this.getWinner());
            default -> Client.getInstance().getLogger().log("Invalid state");
        }
    }
}
