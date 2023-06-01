package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;
import java.util.stream.Collectors;

public class GUIInGame extends GUIState {

    Set<Tile> livingRoomBoard;
    Map<String, Set<Tile>> bookshelves;
    List<Tile> clickedTiles = new ArrayList<>();
    ListView<ImageView> tilesListView = new ListView<>();
    int randomNum;
    private int draggedIndex = -1;


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
        tilesBox.setPrefHeight(50);
        tilesBox.setMinHeight(50);

        tilesListView.setPrefWidth(200);
        tilesListView.setPrefHeight(50);
        tilesListView.setMinHeight(50);
        tilesListView.setOrientation(Orientation.HORIZONTAL);
        tilesListView.setCellFactory(lv -> {
            ListCell<ImageView> cell = new ListCell<ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setGraphic(item);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            cell.setStyle("-fx-background-color: transparent;");
            return cell;
        });

        if (!Client.getInstance().getView().getPickedTiles().isEmpty()) {
            Label pickedTilesLabel = new Label("Picked tiles:");
            pickedTilesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            pickedTilesLabel.setTextFill(Color.BLACK);
            tilesBox.getChildren().add(pickedTilesLabel);
            for (int i = 0; i < Client.getInstance().getView().getPickedTiles().size(); i++) {
                TileType tile = Client.getInstance().getView().getPickedTiles().get(i);
                ImageView tileImage = new ImageView(new Image(getClass().getResource("/assets/tiles/" + tile.toString().toLowerCase() + randomNum + ".png").toExternalForm()));
                tileImage.setFitHeight(40);
                tileImage.setFitWidth(40);
                tileImage.setId(String.valueOf(i));
                tilesListView.getItems().add(tileImage);
            }
            tilesBox.getChildren().add(tilesListView);
        } else {
            tilesListView.getItems().clear();
            tilesBox.getChildren().clear();
        }
        tilesListView.setFixedCellSize(40);

        tilesListView.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        tilesListView.setOnDragDetected(event -> {
            ImageView draggedImageView = tilesListView.getSelectionModel().getSelectedItem();
            if (draggedImageView != null) {
                draggedIndex = tilesListView.getSelectionModel().getSelectedIndex();
                Dragboard db = draggedImageView.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putImage(draggedImageView.getImage());
                db.setContent(content);

                // Create a new WritableImage with the desired dimensions
                double dragWidth = 50;
                double dragHeight = 50;
                WritableImage resizedImage = new WritableImage((int) dragWidth, (int) dragHeight);

                // Draw the original image onto the new WritableImage using a Canvas and GraphicsContext
                Canvas canvas = new Canvas(dragWidth, dragHeight);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.drawImage(draggedImageView.getImage(), 0, 0, dragWidth, dragHeight);

                // Set the new WritableImage as the drag view
                db.setDragView(resizedImage, dragWidth / 2, dragHeight / 2);

                event.consume();
            }
        });


        tilesListView.setOnDragOver(event -> {
            if (event.getGestureSource() != tilesListView && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        tilesListView.setOnDragEntered(event -> {
            if (event.getGestureSource() != tilesListView && event.getDragboard().hasImage()) {
                // Optional: Change the appearance of the ListView while dragging.
            }
            event.consume();
        });

        tilesListView.setOnDragExited(event -> {
            // Optional: Reset the appearance of the ListView after dragging.
            event.consume();
        });

        tilesListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                int dropIndex = calculateDropIndex(event.getX()); // Calculate the drop index based on the x-coordinate
                if (dropIndex >= 0 && dropIndex != draggedIndex) {
                    ImageView draggedImageView = tilesListView.getItems().remove(draggedIndex);
                    tilesListView.getItems().add(dropIndex, draggedImageView);
                    tilesListView.getSelectionModel().select(dropIndex);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        tilesListView.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                ImageView draggedImageView = (ImageView) event.getSource();
                tilesListView.getItems().remove(draggedImageView);
            }
            event.consume();
        });


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

        Scene scene = new Scene(root, 800, 700);
        Platform.runLater(() -> guiApplication.changeScene(scene));
    }

    private int calculateDropIndex(double x) {
        double cellWidth = tilesListView.getFixedCellSize();
        int index = (int) (x / cellWidth);

        if (index < 0) {
            return 0;
        } else if (index >= tilesListView.getItems().size()) {
            return tilesListView.getItems().size() - 1;
        } else {
            return index;
        }
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

        for (String name : Client.getInstance().getView().getPlayers()) {
            StackPane bookshelfGrid = createBookshelfGrid(bookshelves.get(name), name);
            bookshelvesBox.getChildren().add(bookshelfGrid);
        }

        return bookshelvesBox;
    }

    private StackPane createBookshelfGrid(Set<Tile> bookshelfTiles, String bookshelfName) {
        StackPane stackPane = new StackPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(6);
        gridPane.setVgap(3.5);
        gridPane.setPrefSize(180, 216);
        gridPane.setMinSize(180, 216);
        gridPane.setMaxSize(180, 216);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(0, 10, 0, 10));

        Image gridPaneImage = new Image(getClass().getResource("/assets/bookshelf.png").toExternalForm());
        ImageView gridPaneBack = new ImageView(gridPaneImage);
        gridPaneBack.setFitHeight(170);
        gridPaneBack.setFitWidth(160);
        gridPaneBack.setTranslateY(16);

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
                emptyArrow.setFitHeight(18);
                emptyArrow.setFitWidth(18);
                bookshelfArrows.getChildren().add(emptyArrow);
            } else if (Client.getInstance().getNickname().equals(bookshelfName)) {
                ImageView arrow = new ImageView(new Image(getClass().getResource("/assets/arrow.png").toExternalForm()));
                arrow.setFitHeight(18);
                arrow.setFitWidth(18);
                bookshelfArrows.getChildren().add(arrow);
                int finalI = i;
                arrow.setOnMouseEntered(event -> {
                    arrow.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, -0.5, 0));
                });
                arrow.setOnMouseExited(event -> {
                    arrow.setEffect(null);
                });
                arrow.setOnMouseClicked(event -> {
                    List<Integer> order = tilesListView.getItems().stream().map(item -> Integer.parseInt(item.getId())).collect(Collectors.toList());
                    Client.getInstance().getClientController().putTiles(finalI, order);
                    Client.getInstance().getView().setPickedTiles(new ArrayList<>());
                });
            }
        }
        bookshelfArrows.setTranslateY(20);
        bookshelfArrows.setTranslateX(25);

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
        bookshelfNameLabel.setTranslateY(90);

        stackPane.getChildren().addAll(gridPaneBack, gridPane, bookshelfArrows, bookshelfNameLabel);

        return stackPane;
    }
}


