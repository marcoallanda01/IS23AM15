package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.communication.ClientCommunication;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Represents the GUI state for creating a lobby.
 */
public class GUICreateLobby extends GUIState {
    private String nickname;

    /**
     * Constructs a new GUICreateLobby instance.
     *
     * @param guiApplication The GUI application to associate with.
     * @param nickname       The nickname of the user.
     */
    public GUICreateLobby(GUIApplication guiApplication, String nickname) {
        super(guiApplication);
        this.nickname = nickname;
        createUI();
    }

    /**
     * Creates the user interface for the create lobby state.
     */
    private void createUI() {
        VBox root = createRootPane();
        Scene scene = new Scene(root, 800, 600);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    /**
     * Creates the root pane for the create lobby UI.
     *
     * @return The created VBox root pane.
     */
    private VBox createRootPane() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label usernameLabel = createUsernameLabel();
        Button loadGameButton = createLoadGameButton();
        Button newGameButton = createNewGameButton();

        root.getChildren().addAll(usernameLabel, loadGameButton, newGameButton);

        return root;
    }

    /**
     * Creates the username label.
     *
     * @return The created Label to display the username.
     */
    private Label createUsernameLabel() {
        Label usernameLabel = new Label("Welcome, " + nickname + "!");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        return usernameLabel;
    }

    /**
     * Creates the load game button.
     *
     * @return The created Button for loading a game.
     */
    private Button createLoadGameButton() {
        Button loadGameButton = new Button("Load Game");
        loadGameButton.setOnAction(event -> {
            Client.getInstance().getClientController().createLobby(true);
        });
        return loadGameButton;
    }

    /**
     * Creates the new game button.
     *
     * @return The created Button for starting a new game.
     */
    private Button createNewGameButton() {
        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> {
            Client.getInstance().getClientController().createLobby(false);
        });
        return newGameButton;
    }
}
