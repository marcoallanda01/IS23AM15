package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientGoalDetail;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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

/**
 * Generates the GUI for the in game.
 */
@SuppressWarnings("DataFlowIssue")
public class GUIInGame extends GUIState {

    Set<Tile> livingRoomBoard;
    Map<String, Set<Tile>> bookshelves;
    List<Tile> clickedTiles = new ArrayList<>();
    ListView<ImageView> tilesListView = new ListView<>();
    int randomNum;
    private int draggedIndex = -1;

    /**
     * Gui state for when the player is in game
     *
     * @param guiApplication  the guiApplication to use
     * @param livingRoomBoard the living room board
     * @param bookshelves     the bookshelves
     */
    public GUIInGame(GUIApplication guiApplication, Set<Tile> livingRoomBoard, Map<String, Set<Tile>> bookshelves) {
        super(guiApplication);
        this.livingRoomBoard = livingRoomBoard;
        this.bookshelves = bookshelves;
        //Random random = new Random();
        //this.randomNum = random.nextInt(3) + 1;
        this.randomNum = Client.getInstance().getNickname().length() % 3 + 1;
        createUI();
    }

    /**
     * Create the in game UI
     */
    private void createUI() {

        try {
            HBox root = new HBox(10);
            root.setAlignment(Pos.CENTER);

            Image background = new Image(getClass().getResource("/assets/background.jpg").toExternalForm());
            BackgroundSize backgroundSize = new BackgroundSize(300, 300, true, true, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
            root.setBackground(new Background(backgroundImage));

            VBox mainColumn = new VBox(10);
            mainColumn.setPadding(new Insets(10, 10, 10, 10));
            mainColumn.setAlignment(Pos.CENTER);
            root.getChildren().add(mainColumn);

            try {

                HBox topBox = new HBox(10);
                topBox.setAlignment(Pos.CENTER);
                topBox.setPadding(new Insets(10, 10, 10, 10));

                Button chatButton = new Button("Chat");
                chatButton.setOnAction(event -> Client.getInstance().getView().showChat());
                topBox.getChildren().add(chatButton);

                Button saveButton = new Button("Save");
                saveButton.setOnAction(event -> Platform.runLater(() -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Save game");
                    dialog.setHeaderText("Save game");
                    dialog.setContentText("Please enter a name for your save:");
                    dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
                    dialog.setGraphic(null);

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> Client.getInstance().getClientController().saveGame(name));
                }));


                topBox.getChildren().add(saveButton);

                mainColumn.getChildren().add(topBox);


            } catch (Exception e) {
                e.printStackTrace();
            }

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

            VBox tilesBox = new VBox(5);
            tilesBox.setAlignment(Pos.CENTER);

            tilesListView.setOrientation(Orientation.VERTICAL);
            tilesListView.setPrefHeight(150);
            tilesListView.setPrefWidth(60);
            tilesListView.setCellFactory(lv -> {
                ListCell<ImageView> cell = new ListCell<>() {
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
                cell.setAlignment(Pos.CENTER);
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

            VBox goalsBox = createGoals(Client.getInstance().getView().getCommonCards());
            goalsBox.getChildren().add(tilesBox);

            mainColumn.getChildren().addAll(turnBox, boardGrid, bookshelvesBox);

            root.getChildren().addAll(goalsBox);
            if (Client.getInstance().getView().getCurrentTurnPlayer().equals(Client.getInstance().getNickname())) {
                turnBox.getChildren().clear();
                if (Client.getInstance().getView().getPickedTiles().isEmpty()) {
                    if (Client.getInstance().getView().getHasPutTiles()) {
                        Label waitPutLabel = new Label("Ending turn...");
                        waitPutLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                        waitPutLabel.setTextFill(Color.BLACK);
                        turnBox.getChildren().add(waitPutLabel);
                    } else {
                        Label pickLabel = new Label("Pick 1, 2 or 3 tiles");
                        pickLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                        pickLabel.setTextFill(Color.BLACK);
                        turnBox.getChildren().add(pickLabel);
                        turnBox.getChildren().add(submitButton);
                    }
                } else {
                    Label putLabel = new Label("Reorder the picked tiles and select a column of your bookshelf");
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

            Platform.runLater(() -> guiApplication.changeRoot(root));
        } catch (Exception e) {
            Client.getInstance().getLogger().log(e);
        }
    }

    /**
     * Calculates the drop index based on the x-coordinate of the mouse pointer
     *
     * @param x the x-coordinate of the mouse pointer
     * @return the drop index
     */
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


    /**
     * Creates the grid of the board
     *
     * @return the StackPane containing the grid
     */
    private StackPane createBoardGrid() {
        StackPane stackPane = new StackPane();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(1.8);
        gridPane.setVgap(1.8);
        gridPane.setPrefSize(305, 305);
        gridPane.setMinSize(305, 305);
        gridPane.setMaxSize(305, 305);
        gridPane.setAlignment(Pos.CENTER);


        ImageView backgroundImage = new ImageView(getClass().getResource("/assets/livingroomboard.png").toExternalForm());
        backgroundImage.setFitHeight(305);
        backgroundImage.setFitWidth(305);
        backgroundImage.setTranslateX(1.2);
        stackPane.getChildren().add(backgroundImage);

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
                ImageView tileImage = new ImageView(new Image(getClass().getResource("/assets/tiles/" + tile.getType().toString().toLowerCase() + randomNum + ".png").toExternalForm()));
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

    /**
     * Creates the list of goals and the Goal info button
     *
     * @param commonGoals the map containing the goals to be displayed
     * @return the VBox containing the list of goals
     */
    private VBox createGoals(Map<String, List<Integer>> commonGoals) {

        VBox goalsBox = new VBox(5);
        goalsBox.setAlignment(Pos.CENTER);

        Map<String, ClientGoalDetail> goals = Client.getInstance().getView().getGoalsToDetails();

        VBox commonGoalsBox = new VBox(5);
        commonGoalsBox.setAlignment(Pos.CENTER);

        Button button = new Button("Goal info");
        button.setOnAction(e -> Client.getInstance().getView().showGoals());
        HBox buttonContainer = new HBox(button);
        buttonContainer.setAlignment(Pos.TOP_RIGHT);
        commonGoalsBox.getChildren().add(button);

        for (String goal : commonGoals.keySet()) {
            String goalImage = goals.get(goal).getImage();
            StackPane goalStackPane = new StackPane();
            ImageView goalImageView = new ImageView(new Image(getClass().getResource("/assets/goals/common/" + goalImage + ".jpg").toExternalForm()));
            goalImageView.setFitHeight(100);
            goalImageView.setFitWidth(151);
            goalStackPane.getChildren().add(goalImageView);

            List<Integer> tokens = commonGoals.get(goal);
            if (tokens.size() > 0) {
                Integer topToken = tokens.get(tokens.size() - 1);
                ImageView tokenImageView = new ImageView(new Image(getClass().getResource("/assets/goals/common/tokens/" + topToken + ".jpg").toExternalForm()));
                tokenImageView.setFitHeight(40);
                tokenImageView.setFitWidth(40);
                tokenImageView.setRotate(-8);
                tokenImageView.setTranslateX(35);
                tokenImageView.setTranslateY(-3);
                goalStackPane.getChildren().add(tokenImageView);
            }

            commonGoalsBox.getChildren().add(goalStackPane);
        }

        String personalGoalImage = goals.get(Client.getInstance().getView().getPersonalGoal()).getImage();
        ImageView personalGoalImageView = new ImageView(new Image(getClass().getResource("/assets/goals/personal/" + personalGoalImage + ".png").toExternalForm()));
        personalGoalImageView.setFitHeight(150);
        personalGoalImageView.setFitWidth(98.7);
        commonGoalsBox.getChildren().add(personalGoalImageView);

        goalsBox.getChildren().add(commonGoalsBox);

        return goalsBox;
    }

    /**
     * Creates the list of bookshelves
     *
     * @return the HBox containing the list of bookshelves
     */
    private HBox createBookshelves() {
        HBox bookshelvesBox = new HBox(10);
        bookshelvesBox.setAlignment(Pos.CENTER);

        for (String name : Client.getInstance().getView().getPlayers()) {
            StackPane bookshelfGrid = createBookshelfGrid(bookshelves.get(name), name);
            if (name.equals(Client.getInstance().getView().getPlayers().get(0))) {
                ImageView firstPlayerToken = new ImageView(new Image(getClass().getResource("/assets/firstplayertoken.png").toExternalForm()));
                firstPlayerToken.setFitHeight(40);
                firstPlayerToken.setFitWidth(40);
                firstPlayerToken.setTranslateY(125);
                bookshelfGrid.getChildren().add(firstPlayerToken);
            }
            bookshelvesBox.getChildren().add(bookshelfGrid);
        }

        return bookshelvesBox;
    }

    /**
     * Creates a single bookshelf
     *
     * @param bookshelfTiles the set of tiles to be displayed
     * @param bookshelfName  the name of the player owning the bookshelf
     * @return the StackPane containing the bookshelf
     */
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
                ImageView tileImage = new ImageView(new Image(getClass().getResource("/assets/tiles/" + tile.getType().toString().toLowerCase() + randomNum + ".png").toExternalForm()));
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
                arrow.setOnMouseEntered(event -> arrow.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, -0.5, 0)));
                arrow.setOnMouseExited(event -> arrow.setEffect(null));
                arrow.setOnMouseClicked(event -> {
                    List<Integer> order = tilesListView.getItems().stream().map(item -> Integer.parseInt(item.getId())).collect(Collectors.toList());
                    if (!order.isEmpty()) {
                        Collections.reverse(order);
                        Client.getInstance().getClientController().putTiles(finalI, order);
                    }
                });
            }
        }
        bookshelfArrows.setTranslateY(20);
        bookshelfArrows.setTranslateX(25);

        bookshelfName = bookshelfName.substring(0, Math.min(bookshelfName.length(), 8));
        Map<String, Integer> points = Client.getInstance().getView().getPoints();
        Label bookshelfNameLabel = new Label(bookshelfName + ": " + (points.get(bookshelfName) == null ? 0 : String.valueOf(points.get(bookshelfName))));
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


