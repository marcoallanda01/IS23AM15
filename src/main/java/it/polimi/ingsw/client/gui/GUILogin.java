package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.communication.ClientCommunication;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GUILogin extends GUIState {
    public GUILogin(GUIApplication guiApplication) {
        super(guiApplication);
        // Create the layout pane
        StackPane root = new StackPane();
        root.setPadding(new Insets(10));

        // Create the label for the username input
        Label label = new Label("Enter your username:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Create the text field for username input
        TextField usernameField = createUsernameField();
        usernameField.setMaxWidth(500);

        // Create the login button
        Button loginButton = createLoginButton(usernameField);
        loginButton.setMinWidth(100);
        loginButton.setStyle("-fx-font-size: 14px;");

        VBox container = new VBox(10);
        container.getChildren().addAll(label, usernameField, loginButton);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(Double.MAX_VALUE);
        container.setFillWidth(false);

        root.getChildren().add(container);

        // Create the scene and set the root pane
        Scene scene = new Scene(root, 800, 600);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }


    private TextField createUsernameField() {
        TextField textField = new TextField();
        textField.setPromptText("Username");
        return textField;
    }

    private Button createLoginButton(TextField usernameField) {
        Button button = new Button("Login");
        button.setOnAction(event -> {
            String username = usernameField.getText();
            if (!username.isEmpty()) {
                // Call the login method with the entered username
                Client.getInstance().getClientController().login(username);
            }
        });
        return button;
    }
}
