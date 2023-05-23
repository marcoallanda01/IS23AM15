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
    private List<String> lobbyPlayers;
    private final int maxPlayers;

    public GUILobby(GUIApplication guiApplication, List<String> lobbyPlayers, int maxPlayers) {
        super(guiApplication);
        this.lobbyPlayers = lobbyPlayers;
        this.maxPlayers = maxPlayers;
        createUI();
    }
    public void createUI(){
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Lobby");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label playersLabel = new Label("Players in lobby:");
        playersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        VBox lobbyPlayersBox = createLobbyPlayersBox();

        Label remainingPlayersLabel = new Label("Remaining players: " + (maxPlayers - lobbyPlayers.size()));
        remainingPlayersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        root.getChildren().addAll(titleLabel, playersLabel, lobbyPlayersBox, remainingPlayersLabel);

        Scene scene = new Scene(root, 800, 600);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    private VBox createLobbyPlayersBox() {
        VBox lobbyPlayersBox = new VBox(10);
        lobbyPlayersBox.setPadding(new Insets(10));
        lobbyPlayersBox.setAlignment(Pos.CENTER);

        for(String lobbyPlayer : lobbyPlayers) {
            Label lobbyPlayerLabel = new Label(lobbyPlayer);
            lobbyPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            lobbyPlayersBox.getChildren().add(lobbyPlayerLabel);
        }

        return lobbyPlayersBox;
    }
}
