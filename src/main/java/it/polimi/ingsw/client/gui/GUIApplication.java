package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

public class GUIApplication extends Application {
    private Stage primaryStage;

    @Override
    public synchronized void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MyShelfie");
        this.primaryStage.initStyle(StageStyle.UNIFIED);

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

    @Override
    public void stop() throws Exception {
        super.stop();
        Client.getInstance().getLogger().log("Application closed");
        Client.getInstance().getClientController().logout();
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

    public synchronized void changeScene(Parent newRoot) {
        // Set the root node of the new content opacity to 1.0
        newRoot.setOpacity(1.0);
        System.out.println("primaryStage.getScene() = " + primaryStage.getScene().getRoot());

        // Update the root node of the current scene
        if (primaryStage.getScene().getRoot() instanceof VBox) {
            VBox currentRoot = (VBox) primaryStage.getScene().getRoot();

            currentRoot.getChildren().clear();
            currentRoot.getChildren().setAll(newRoot);
        } else if (primaryStage.getScene().getRoot() instanceof StackPane) {
            StackPane currentRoot = (StackPane) primaryStage.getScene().getRoot();

            currentRoot.getChildren().clear();
            currentRoot.getChildren().setAll(newRoot);
        } else {
            throw new IllegalStateException("Unexpected root node type: " + primaryStage.getScene().getRoot().getClass().getName());
        }

    }

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

    public void showPopup(String error) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.WINDOW_MODAL);
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

