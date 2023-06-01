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

/**
 * Generates the GUI for the login state.
 */
public class GUILogin extends GUIState {

    /**
     * Constructs a new GUILogin instance.
     *
     * @param guiApplication The GUI application to associate with.
     */
    public GUILogin(GUIApplication guiApplication) {
        super(guiApplication);
        createUI();
    }

    /**
     * Creates the user interface for the login state.
     */
    private void createUI() {
        StackPane root = createRootPane();
        Scene scene = new Scene(root, 800, 700);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    /**
     * Creates the root pane for the login UI.
     *
     * @return The created StackPane root pane.
     */
    private StackPane createRootPane() {
        StackPane root = new StackPane();
        root.setPadding(new Insets(10));

        Label label = createUsernameLabel();
        TextField usernameField = createUsernameField();
        Button loginButton = createLoginButton(usernameField);

        VBox container = createContainer(label, usernameField, loginButton);
        root.getChildren().add(container);

        return root;
    }

    /**
     * Creates the username label.
     *
     * @return The created Label for the username.
     */
    private Label createUsernameLabel() {
        Label label = new Label("Enter your username:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        return label;
    }

    /**
     * Creates the username input field.
     *
     * @return The created TextField for the username input.
     */
    private TextField createUsernameField() {
        TextField textField = new TextField();
        textField.setPromptText("Username");
        textField.setMaxWidth(500);
        return textField;
    }

    /**
     * Creates the login button.
     *
     * @param usernameField The TextField for the username input.
     * @return The created Button for login.
     */
    private Button createLoginButton(TextField usernameField) {
        Button button = new Button("Login");
        button.setOnAction(event -> {
            String username = usernameField.getText();
            if (!username.isEmpty()) {
                Client.getInstance().getClientController().login(username);
            }
        });
        button.setMinWidth(100);
        button.setStyle("-fx-font-size: 14px;");
        return button;
    }

    /**
     * Creates the container VBox for the UI components.
     *
     * @param label         The Label for the username.
     * @param usernameField The TextField for the username input.
     * @param loginButton   The Button for login.
     * @return The created VBox container.
     */
    private VBox createContainer(Label label, TextField usernameField, Button loginButton) {
        VBox container = new VBox(10);
        container.getChildren().addAll(label, usernameField, loginButton);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(Double.MAX_VALUE);
        container.setFillWidth(false);
        return container;
    }
}