package it.polimi.ingsw.client.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates the GUI for the end game.
 */
public class GUIEndGame extends GUIState {
    private Map<String, Integer> playerPoints;
    private String winner;

    /**
     * Constructs a new GUIEndGame instance.
     *
     * @param guiApplication The GUI application to associate with.
     * @param playerPoints   The map of player names to their points.
     * @param winner         The name of the winner.
     */
    public GUIEndGame(GUIApplication guiApplication, Map<String, Integer> playerPoints, String winner) {
        super(guiApplication);
        this.playerPoints = playerPoints;
        this.winner = winner;
        createUI();
    }

    /**
     * Creates the user interface for the end game state.
     */
    private void createUI() {
        VBox root = createRootPane();
        Scene scene = new Scene(root, 800, 700);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    /**
     * Creates the root pane for the end game UI.
     *
     * @return The created VBox root pane.
     */
    private VBox createRootPane() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = createTitleLabel();
        root.getChildren().add(titleLabel);

        LinkedHashMap<String, Integer> sortedPoints = playerPoints.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        for (String player : sortedPoints.keySet()) {
            if (playerPoints.containsKey(player)) {
                Label playerLabel = createPlayerLabel(player, playerPoints.get(player));
                root.getChildren().add(playerLabel);
            }
        }

        Label winnerLabel = createWinnerLabel(winner);
        root.getChildren().add(winnerLabel);

        return root;
    }
    /**
     * Creates the winner label for the end game UI.
     *
     * @return The created Label to display the winner.
     */
    private Label createWinnerLabel(String winner) {
        Label winnerLabel = new Label("Winner: " + winner);
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        return winnerLabel;
    }

    /**
     * Creates the title label for the end game UI.
     *
     * @return The created Label to display the title.
     */
    private Label createTitleLabel() {
        Label titleLabel = new Label("Game Over");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        return titleLabel;
    }

    /**
     * Creates a label for displaying a player's points.
     *
     * @param playerName The name of the player.
     * @param points     The points of the player.
     * @return The created Label to display the player's points.
     */
    private Label createPlayerLabel(String playerName, int points) {
        Label playerLabel = new Label(playerName + ": " + points + " points");
        playerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        return playerLabel;
    }
}