package it.polimi.ingsw.client.gui;

import java.util.List;
import it.polimi.ingsw.client.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GUILoadGame extends GUIState{
    private final List<String> savedGames;

    /**
     * Constructor for GUILoadGame
     * @param guiApplication is the GUIApplication
     * @param savedGames is the list of saved games
     */
    public GUILoadGame(GUIApplication guiApplication, List<String> savedGames) {
        super(guiApplication);
        this.savedGames = savedGames;
        createUI();
    }

    /**
     * Method to create the UI
     */
    public void createUI(){
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Load Game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label savedGamesLabel = new Label("Saved Games:");
        savedGamesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        VBox savedGamesBox = createSavedGamesBox();

        root.getChildren().addAll(titleLabel, savedGamesLabel, savedGamesBox);

        Scene scene = new Scene(root, 800, 700);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    /**
     * Method to create the VBox of saved games
     * @return the VBox of saved games
     */
    private VBox createSavedGamesBox() {
    VBox savedGamesBox = new VBox(10); // Added spacing between buttons
    savedGamesBox.setPadding(new Insets(10));
    savedGamesBox.setAlignment(Pos.CENTER);

    for (String savedGame : savedGames) {
        Button savedGameButton = new Button(savedGame);
        int savedGameIndex = savedGames.indexOf(savedGame);
        savedGameButton.setOnAction(event -> {
            Client.getInstance().getClientController().loadGame(savedGameIndex);
        });
        savedGamesBox.getChildren().add(savedGameButton);
    }

    return savedGamesBox;
}
}
