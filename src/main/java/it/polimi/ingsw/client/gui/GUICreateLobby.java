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

public class GUICreateLobby extends GUIState {
    private String nickname;

    public GUICreateLobby(GUIApplication guiApplication, String nickname) {
        super(guiApplication);
        this.nickname = nickname;
        createUI();
    }

    private void createUI() {
        // Create the layout pane
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        // Create the label to display the username
        Label usernameLabel = new Label("Welcome, " + nickname + "!");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Create the buttons
        Button loadGameButton = createLoadGameButton();
        Button newGameButton = createNewGameButton();

        // Add the components to the root pane
        root.getChildren().addAll(usernameLabel, loadGameButton, newGameButton);

        // Create the scene and set the root pane
        Scene scene = new Scene(root, 400, 300);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    private Button createLoadGameButton() {
        Button loadGameButton = new Button("Load Game");
        loadGameButton.setOnAction(event -> {
            Client.getInstance().getClientController().createLobby(true);
        });
        return loadGameButton;
    }

    private Button createNewGameButton() {
        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> {
            Client.getInstance().getClientController().createLobby(false);
        });
        return newGameButton;
    }
}
