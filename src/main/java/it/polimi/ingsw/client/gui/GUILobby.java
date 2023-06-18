package it.polimi.ingsw.client.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class GUILobby extends GUIState{
    private final boolean isEasyRules;

    /**
     * Constructor for GUILobby
     * @param guiApplication is the GUIApplication
     * @param isEasyRules is true if the game will use easy rules, false otherwise
     */
    public GUILobby(GUIApplication guiApplication, boolean isEasyRules) {
        super(guiApplication);
        this.isEasyRules = isEasyRules;
        createUI();
    }

    /**
     * Method to create the UI
     */
    public void createUI(){
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Lobby");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label playersLabel = new Label("Waiting in lobby for other players to join...");
        playersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label rulesLabel = new Label("The game will use " + (isEasyRules ? "easy" : "standard") + " rules");
        rulesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        root.getChildren().addAll(titleLabel, playersLabel, rulesLabel);

        Scene scene = new Scene(root, 800, 700);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }
}
