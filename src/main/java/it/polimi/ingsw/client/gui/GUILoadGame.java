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

    public GUILoadGame(GUIApplication guiApplication, List<String> savedGames) {
        super(guiApplication);
        this.savedGames = savedGames;
        createUI();
    }
    public void createUI(){
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Load Game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label savedGamesLabel = new Label("Saved Games:");
        savedGamesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        StackPane savedGamesPane = createSavedGamesPane();

        root.getChildren().addAll(titleLabel, savedGamesLabel, savedGamesPane);

        Scene scene = new Scene(root, 800, 600);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    private StackPane createSavedGamesPane() {
        StackPane savedGamesPane = new StackPane();
        savedGamesPane.setPadding(new Insets(10));
        savedGamesPane.setAlignment(Pos.CENTER);

        for(String savedGame : savedGames) {
            Button savedGameButton = new Button(savedGame);
            int savedGameIndex = savedGames.indexOf(savedGame);
            savedGameButton.setOnAction(event -> {
                Client.getInstance().getClientController().loadGame(savedGameIndex);
            });
            savedGamesPane.getChildren().add(savedGameButton);
        }

        return savedGamesPane;
    }
}
