package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.communication.ClientCommunication;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUILogin extends GUIState {
    public GUILogin(GUIApplication guiApplication) {
        super(guiApplication);
        // Create the start button
        Button startButton = createStartButton();

        // Create the layout pane
        StackPane root = new StackPane();
        root.getChildren().add(startButton);

        // Create the scene and set the root pane
        Scene scene = new Scene(root, 400, 300);

        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    private Button createStartButton() {
        Button button = new Button("Start");
        button.setOnAction(event -> {
            String nickname = promptForNickname();
            if (nickname != null && !nickname.isEmpty()) {
                // Call the login method with the entered nickname
                Client.getInstance().getClientController().login(nickname);
            }
        });
        return button;
    }

    private String promptForNickname() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Nickname");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your nickname:");

        // Show the dialog and wait for user input
        dialog.showAndWait();

        // Return the entered nickname or null if cancelled
        return dialog.getResult();
    }
}
