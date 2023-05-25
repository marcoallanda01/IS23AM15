package it.polimi.ingsw.client.gui;
import it.polimi.ingsw.client.Client;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class GUIApplication extends Application {
    private Stage primaryStage;

    @Override
    public synchronized void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MyShelfie");

        // Create the text node
        Text text = new Text("MyShelfie is loading");
        text.setFont(Font.font("Arial", 48)); // Set the font size to 48

        // Create the layout pane
        StackPane root = new StackPane();
        root.getChildren().add(text);

        // Create the scene and set the root pane
        Scene scene = new Scene(root, 800, 600);

        // Set CSS styling for the text
        text.getStyleClass().add("my-shelfie-text");

        // Set the scene to the primary stage
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeViewAndConnection(5);
    }

    public void clearStage() {
        // Get the root node of the stage
        StackPane root = (StackPane) primaryStage.getScene().getRoot();

        // Clear the content of the root node
        root.getChildren().clear();
    }

    public Stage clearAndGetStage() {
        clearStage();
        return primaryStage;
    }

    public synchronized void transitionToScene(Scene newScene) {
        // Create a fade-out transition for the current scene
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), primaryStage.getScene().getRoot());
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Create a fade-in transition for the new scene
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(1), newScene.getRoot());
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

    private void initializeViewAndConnection(int countdown) {
        try {
            GUI gui = new GUI(this);
            Client.getInstance().init(gui);
            gui.render();
        } catch (Exception e) {
            clearStage();
            AtomicInteger retryCount = new AtomicInteger(countdown); // Initial retry count
            Text text = new Text("MyShelfie encountered an error while starting, will retry in: " + retryCount.get());
            text.setFont(Font.font("Arial", 18));

            // Create the layout pane
            StackPane root = new StackPane();
            root.getChildren().add(text);

            // Create the scene and set the root pane
            Scene scene = new Scene(root, 800, 600);

            // Set CSS styling for the text
            text.getStyleClass().add("my-shelfie-text");

            // Set the scene to the primary stage
            transitionToScene(scene);
            // Create a Timeline animation to update the retry countdown
            Timeline timeline = new Timeline();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
                int count = retryCount.decrementAndGet();
                text.setText("MyShelfie encountered an error while starting, will retry in: " + count);
                if (count == 0) {
                    initializeViewAndConnection(countdown * 2);
                    timeline.stop();
                }
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.setCycleCount(retryCount.get());
            timeline.play();
        }
    }

    public void showPopup(String error) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNDECORATED);

        Label messageLabel = new Label(error);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> popupStage.close());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(messageLabel, closeButton);

        // Set the background color of the layout
        BackgroundFill backgroundFill = new BackgroundFill(Color.RED, null, null);
        Background background = new Background(backgroundFill);
        layout.setBackground(background);

        StackPane container = new StackPane();
        container.getChildren().addAll(primaryStage.getScene().getRoot(), layout);

        Scene popupScene = new Scene(container);

        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}

