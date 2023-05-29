package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GUIInGame extends GUIState {

    Set<Tile> livingRoomBoard;
    Map<String, Set<Tile>> bookshelves;
    List<Tile> clickedTiles = new ArrayList<>();

    int randomNum;


    public GUIInGame(GUIApplication guiApplication, Set<Tile> livingRoomBoard, Map<String, Set<Tile>> bookshelves) {
        super(guiApplication);
        this.livingRoomBoard = livingRoomBoard;
        this.bookshelves = bookshelves;
        Random random = new Random();
        this.randomNum = random.nextInt(3) + 1;
        createUI();
    }

    private void createUI() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.CENTER);
        Image background = new Image(getClass().getResource("/assets/background.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        StackPane boardGrid = createBoardGrid();
        HBox bookshelvesBox = createBookshelves();
        HBox turnBox = new HBox(5);
        turnBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            List<List<Integer>> clickedTilesCoords = new ArrayList<>();
            for (Tile tile : clickedTiles) {
                clickedTilesCoords.add(Arrays.asList(tile.getX(), tile.getY()));
            }
            Client.getInstance().getClientController().pickTiles(clickedTilesCoords);
            clickedTiles.clear();
        });
        HBox tilesBox = new HBox(5);
        tilesBox.setAlignment(Pos.CENTER);
        if (!Client.getInstance().getView().getPickedTiles().isEmpty()) {
            Label pickedTilesLabel = new Label("Picked tiles:");
            pickedTilesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            pickedTilesLabel.setTextFill(Color.BLACK);
            tilesBox.getChildren().add(pickedTilesLabel);
            for (TileType tile : Client.getInstance().getView().getPickedTiles()) {
                ImageView tileImage = new ImageView(new Image(getClass().getResource("/assets/tiles/" + tile.toString().toLowerCase() + randomNum + ".png").toExternalForm()));
                tileImage.setFitHeight(40);
                tileImage.setFitWidth(40);
                tilesBox.getChildren().add(tileImage);
            }
        } else {
            tilesBox.getChildren().clear();
        }

        root.getChildren().addAll(turnBox, boardGrid, tilesBox, bookshelvesBox);
        if (Client.getInstance().getView().getCurrentTurnPlayer().equals(Client.getInstance().getNickname())) {
            turnBox.getChildren().clear();
            if (Client.getInstance().getView().getPickedTiles().isEmpty()) {
                Label pickLabel = new Label("Pick 1, 2 or 3 tiles");
                pickLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                pickLabel.setTextFill(Color.BLACK);
                turnBox.getChildren().add(pickLabel);
                turnBox.getChildren().add(submitButton);
            } else {
                Label putLabel = new Label("Select an order and a column of your bookshelf");
                putLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                putLabel.setTextFill(Color.BLACK);
                turnBox.getChildren().add(putLabel);
            }
        } else {
            turnBox.getChildren().clear();
            Label waitingLabel = new Label("Waiting for " + Client.getInstance().getView().getCurrentTurnPlayer() + " turn...");
            waitingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            waitingLabel.setTextFill(Color.BLACK);
            turnBox.getChildren().add(waitingLabel);
        }

        Scene scene = new Scene(root, 800, 600);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    private StackPane createBoardGrid() {
        StackPane stackPane = new StackPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(1.7);
        gridPane.setVgap(1.7);
        gridPane.setPrefSize(305, 305);
        gridPane.setMinSize(305, 305);
        gridPane.setMaxSize(305, 305);
        gridPane.setAlignment(Pos.CENTER);


        Image backgroundImage = new Image(getClass().getResource("/assets/livingroomboard.png").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, true, false);
        BackgroundImage backgroundImageView = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        gridPane.setBackground(new Background(backgroundImageView));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ImageView emptyTile = new ImageView();
                emptyTile.setFitHeight(29);
                emptyTile.setFitWidth(29);
                GridPane.setConstraints(emptyTile, j, i);
                gridPane.getChildren().add(emptyTile);
            }
        }
        for (Tile tile : livingRoomBoard) {
            if (tile != null) {
                ImageView tileImage = new ImageView(new Image(getClass().getResource("/assets/tiles/" + tile.getType() + randomNum + ".png").toExternalForm()));
                tileImage.setFitHeight(29);
                tileImage.setFitWidth(29);
                GridPane.setConstraints(tileImage, tile.getY(), tile.getX());

                if (Client.getInstance().getView().getCurrentTurnPlayer().equals(Client.getInstance().getNickname())) {
                    tileImage.setOnMouseClicked(event -> {
                        if (tileImage.getEffect() == null) {
                            tileImage.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, -0.5, 0));
                            clickedTiles.add(tile);
                        } else {
                            tileImage.setEffect(null);
                            clickedTiles.remove(tile);
                        }

                    });
                }

                gridPane.getChildren().add(tileImage);
            }
        }

        stackPane.getChildren().addAll(gridPane);
        StackPane.setAlignment(gridPane, Pos.CENTER);


        return stackPane;
    }


    private HBox createBookshelves() {
        HBox bookshelvesBox = new HBox(10);
        bookshelvesBox.setAlignment(Pos.CENTER);

        for (Map.Entry<String, Set<Tile>> entry : bookshelves.entrySet()) {
            StackPane bookshelfGrid = createBookshelfGrid(entry.getValue(), entry.getKey());
            bookshelvesBox.getChildren().add(bookshelfGrid);
        }

        return bookshelvesBox;
    }

    private StackPane createBookshelfGrid(Set<Tile> bookshelfTiles, String bookshelfName) {
        StackPane stackPane = new StackPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(7);
        gridPane.setVgap(2);
        gridPane.setPrefSize(180, 216);
        gridPane.setMinSize(180, 216);
        gridPane.setMaxSize(180, 216);
        gridPane.setAlignment(Pos.CENTER);

        Image backgroundImage = new Image(getClass().getResource("/assets/bookshelf.png").toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(0.8, 1, true, true, true, false);
        BackgroundImage backgroundImageView = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        gridPane.setBackground(new Background(backgroundImageView));

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                ImageView emptyTile = new ImageView();
                emptyTile.setFitHeight(20);
                emptyTile.setFitWidth(20);
                GridPane.setConstraints(emptyTile, j, i);
                gridPane.getChildren().add(emptyTile);
            }
        }
        for (Tile tile : bookshelfTiles) {
            if (tile != null) {
                ImageView tileImage = new ImageView(new Image(getClass().getResource("/assets/tiles/" + tile.getType() + randomNum + ".png").toExternalForm()));
                tileImage.setFitHeight(20);
                tileImage.setFitWidth(20);
                gridPane.add(tileImage, tile.getX(), 6 - tile.getY());
            }
        }

        HBox bookshelfArrows = new HBox(10);
        for (int i = 0; i < 5; i++) {
            if (Client.getInstance().getView().getPickedTiles().isEmpty()) {
                ImageView emptyArrow = new ImageView();
                emptyArrow.setFitHeight(20);
                emptyArrow.setFitWidth(20);
                bookshelfArrows.getChildren().add(emptyArrow);
            } else if (Client.getInstance().getNickname().equals(bookshelfName)) {
                ImageView arrow = new ImageView(new Image(getClass().getResource("/assets/arrow.png").toExternalForm()));
                arrow.setFitHeight(20);
                arrow.setFitWidth(20);
                bookshelfArrows.getChildren().add(arrow);
                int finalI = i;
                arrow.setOnMouseEntered(event -> {
                    arrow.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, -0.5, 0));
                });
                arrow.setOnMouseExited(event -> {
                    arrow.setEffect(null);
                });
                arrow.setOnMouseClicked(event -> {
                    Client.getInstance().getClientController().putTiles(finalI, IntStream.rangeClosed(0, Client.getInstance().getView().getPickedTiles().size() - 1).boxed().collect(Collectors.toList()));
                    Client.getInstance().getView().setPickedTiles(new ArrayList<>());
                });
            }
        }
        bookshelfArrows.setTranslateY(-5);
        bookshelfArrows.setTranslateX(20);
        bookshelfArrows.setTranslateZ(1);

        bookshelfName = bookshelfName.substring(0, Math.min(bookshelfName.length(), 8));
        Label bookshelfNameLabel = new Label(bookshelfName);
        bookshelfNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        if (bookshelfName.equals(Client.getInstance().getView().getCurrentTurnPlayer())) {
            if (bookshelfName.equals(Client.getInstance().getNickname())) {
                bookshelfNameLabel.setTextFill(Color.RED);
                bookshelfNameLabel.setUnderline(true);
            } else {
                bookshelfNameLabel.setTextFill(Color.RED);
            }
        } else {
            if (bookshelfName.equals(Client.getInstance().getNickname())) {
                bookshelfNameLabel.setTextFill(Color.WHITE);
                bookshelfNameLabel.setUnderline(true);
            } else {
                bookshelfNameLabel.setTextFill(Color.WHITE);
            }
        }
        bookshelfNameLabel.setTranslateX(0);
        bookshelfNameLabel.setTranslateY(80);

        stackPane.getChildren().addAll(gridPane,bookshelfArrows, bookshelfNameLabel);

        return stackPane;
    }


}
