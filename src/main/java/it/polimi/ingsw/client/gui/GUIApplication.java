package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.stage.Screen;

public class GUIApplication extends Application {
    private Stage primaryStage;

    private final Stage chatStage = new Stage();

    @Override
    public synchronized void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MyShelfie");
        this.primaryStage.initStyle(StageStyle.UNIFIED);

        this.chatStage.initModality(Modality.NONE);
        this.chatStage.initOwner(primaryStage);
        this.chatStage.setAlwaysOnTop(true);

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
    public synchronized void stop() throws Exception {
        super.stop();
        Client.getInstance().getLogger().log("Application closed");
        Runtime.getRuntime().exit(0);
    }
    public synchronized void stop(String message) throws Exception {
        showPopup(message, () -> {
            Platform.exit();
            Runtime.getRuntime().exit(0);
        });
    }

    public synchronized void clearStage() {
        // Get the root node of the stage
        StackPane root = (StackPane) primaryStage.getScene().getRoot();

        // Clear the content of the root node
        root.getChildren().clear();
    }

    public synchronized Stage clearAndGetStage() {
        clearStage();
        return primaryStage;
    }

    public synchronized void changeScene(Scene newScene) {
        primaryStage.setScene(newScene);
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

    public void showPopup(String message) {
        showPopup(message, () -> {});
    }

    public void showChat(Scene chatScene){
        chatStage.setScene(chatScene);
        chatStage.show();
    }

    public void showPopup(String message, Runnable onCloseFunction) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);
        popupStage.toFront();

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
        } else if  (primaryStageY < 0)  {
            primaryStageY = 0;
        }

        popupStage.setX(primaryStageX + 50);
        popupStage.setY(primaryStageY + 50);

        popupStage.showAndWait();
    }
}

