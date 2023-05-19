package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.server.model.Tile;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GUIInGame extends GUIState {

    Set<Tile> livingRoomBoard;
    Map<String, Set<Tile>> bookshelves;

    public GUIInGame(GUIApplication guiApplication, Set<Tile> livingRoomBoard, Map<String, Set<Tile>> bookshelves) {
        super(guiApplication);
        this.livingRoomBoard = livingRoomBoard;
        this.bookshelves = bookshelves;
        createUI();
    }

    private void createUI() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Living Room Board");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        StackPane boardGrid = createBoardGrid();

        root.getChildren().addAll(titleLabel, boardGrid);

        Scene scene = new Scene(root, 800, 600);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    private StackPane createBoardGrid() {
        StackPane stackPane = new StackPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(1.7);
        gridPane.setVgap(1.7);
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(Pos.CENTER);

        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource("/assets/livingroomboard.png").toExternalForm()));
        backgroundImageView.setFitHeight(315);
        backgroundImageView.setFitWidth(315);
        backgroundImageView.setPreserveRatio(true);

        Random random = new Random();
        for (Tile tile : livingRoomBoard) {
            if (tile != null) {
                int randomNum = random.nextInt(3) + 1;
                ImageView tileImage = new ImageView(new Image(getClass().getResource("/assets/tiles/" + tile.getType() + randomNum + ".png").toExternalForm()));
                tileImage.setFitHeight(30);
                tileImage.setFitWidth(30);
                gridPane.add(tileImage, tile.getY(), tile.getX());
            }
        }

        stackPane.getChildren().addAll(backgroundImageView, gridPane);
        StackPane.setMargin(gridPane, new Insets(0, 0, 0, -6));

        return stackPane;
    }

}
