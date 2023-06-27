package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientGoalDetail;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.stage.Screen;

import java.util.List;

/**
 * GUIApplication that renders
 */
public class GUIApplication extends Application {
    private Stage primaryStage;
    private final Stage chatStage = new Stage();
    private final Stage goalStage = new Stage();
    private Stage primaryStage;

    /**
     * Constructor of GUI application
     *
     * @param primaryStage the injected stage
     */
    @Override
    public synchronized void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MyShelfie");
        this.primaryStage.initStyle(StageStyle.UNIFIED);

        this.chatStage.initModality(Modality.NONE);
        this.chatStage.initOwner(primaryStage);
        this.chatStage.setAlwaysOnTop(true);

        this.goalStage.initModality(Modality.NONE);
        this.goalStage.initOwner(primaryStage);
        this.goalStage.setAlwaysOnTop(true);

        // Create the text node
        Text text = new Text("MyShelfie is loading");
        text.setFont(Font.font("Arial", 48)); // Set the font size to 48

        // Create the layout pane
        StackPane root = new StackPane();
        root.getChildren().add(text);

        // Create the scene and set the root pane
        Scene scene = new Scene(root, 800, 700);

        // Set CSS styling for the text
        text.getStyleClass().add("my-shelfie-text");

        // Set the scene to the primary stage
        primaryStage.setScene(scene);
        primaryStage.show();

        new GUI(this);
    }

    /**
     * closes the GUI application
     * @throws Exception
     */
    @Override
    public synchronized void stop() throws Exception {
        super.stop();
        Client.getInstance().getLogger().log("Application closed");
        Runtime.getRuntime().exit(0);
    }
    /**
     * creates a prompt that displays a message anda a button that when clicked closes the gui application
     * @throws Exception
     */
    public synchronized void stop(String message) throws Exception {
        showPopup(message, () -> {
            Platform.exit();
            Runtime.getRuntime().exit(0);
        });
    }

    /**
     * Changes the scene with transition
     *
     * @param newScene the new scene
     */
    public synchronized void transitionToScene(Scene newScene) {
        // Create a fade-out transition for the current scene
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.4), primaryStage.getScene().getRoot());
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Create a fade-in transition for the new scene
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.4), newScene.getRoot());
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        // Combine the fade transitions in a parallel transition
        ParallelTransition transition = new ParallelTransition(fadeOutTransition, fadeInTransition);
        transition.setOnFinished(event -> {
            // Set the new scene after the transition
            primaryStage.setScene(newScene);
        });
        transition.play();
    }
    /**
     * shows a popup that only closes upon interaction
     * @param message the message to show in the popup
     */
    public void showPopup(String message) {
        showPopup(message, () -> {
        });
    }

    /**
     * Shows the chat
     *
     * @param chatScene   the new chat scene
     * @param forceActive should the chat be shown even if it is not open?
     */
    public void showChat(Scene chatScene, boolean forceActive) {
        if (forceActive || chatStage.isShowing()) {
            chatStage.setScene(chatScene);
            chatStage.show();
        } else {
            showPopup("There is a new message in the chat, open the chat to see it");
        }
    }

    /**
     * shows a popup
     *
     * @param message         the message to show in the popup
     * @param onCloseFunction the function to execute once the popup is closed
     */
    public void showPopup(String message, Runnable onCloseFunction) {
        Stage popupStage = new Stage();

        popupStage.initModality(Modality.NONE);
        popupStage.initOwner(primaryStage);
        popupStage.setAlwaysOnTop(true);

        Label messageLabel = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
            onCloseFunction.run();
            popupStage.close();
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(messageLabel, closeButton);

        Scene popupScene = new Scene(layout);
        popupStage.setScene(popupScene);

        // Get the primary screen
        Screen screen = Screen.getPrimary();

        // Retrieve the screen bounds
        Rectangle2D bounds = screen.getVisualBounds();

        // Retrieve the screen width
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();

        double primaryStageX = primaryStage.getX();
        double primaryStageY = primaryStage.getY();

        if (primaryStageX > screenWidth) {
            primaryStageX = screenWidth;
        } else if (primaryStageX < 0)  {
            primaryStageX = 0;
        }

        if (primaryStageY > screenHeight) {
            primaryStageY = screenHeight;
        } else if (primaryStageY < 0) {
            primaryStageY = 0;
        }

        popupStage.setX(primaryStageX + 50);
        popupStage.setY(primaryStageY + 50);

        popupStage.showAndWait();
    }

    /**
     * Creates a window with the goals and their info
     *
     * @param commonCards  the commonCards
     * @param commonGoals  the commonGoals
     * @param personalGoal the personalGoal
     */
    public void printGoals(List<ClientGoalDetail> commonCards, List<ClientGoalDetail> commonGoals, ClientGoalDetail personalGoal) {
        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        contentBox.setPadding(new Insets(20));
        contentBox.setMaxWidth(550);

        Text commonCardsTitle = new Text("Common cards:");
        commonCardsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        contentBox.getChildren().add(commonCardsTitle);

        for (ClientGoalDetail goal : commonCards) {
            Text goalText = new Text(goal.getName() + ": " + goal.getDescription());
            goalText.setWrappingWidth(contentBox.getMaxWidth());
            contentBox.getChildren().add(goalText);
        }

        Text commonGoalsTitle = new Text("Common goals:");
        commonGoalsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        contentBox.getChildren().add(commonGoalsTitle);

        for (ClientGoalDetail goal : commonGoals) {
            Text goalText = new Text(goal.getName() + ": " + goal.getDescription());
            goalText.setWrappingWidth(contentBox.getMaxWidth());
            contentBox.getChildren().add(goalText);
        }

        Text personalGoalTitle = new Text("Personal goal:");
        personalGoalTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        contentBox.getChildren().add(personalGoalTitle);

        Text personalGoalText = new Text(personalGoal.getName() + ": " + personalGoal.getDescription());
        personalGoalText.setWrappingWidth(contentBox.getMaxWidth());
        contentBox.getChildren().add(personalGoalText);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> goalStage.close());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().add(closeButton);

        contentBox.getChildren().add(closeButton);

        goalStage.setTitle("Goal details");
        goalStage.setScene(new Scene(scrollPane, 600, 500));
        goalStage.show();
    }

    /**
     * Changes the root of the scene
     *
     * @param root the new root
     */
    public void changeRoot(HBox root) {
        primaryStage.getScene().setRoot(root);
    }
}

