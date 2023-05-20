package it.polimi.ingsw.client.gui;
import it.polimi.ingsw.client.Client;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        GUI gui = new GUI(this);
        Client.getInstance().init(gui);
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
        System.out.println("Transitioning");
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

    public void showPopup(String error) {
        // Create the text node
        Text text = new Text(error);
        text.setFont(Font.font("Arial", 18));

        // Create the layout pane
        StackPane root = new StackPane();
        root.getChildren().add(text);

        // Create the scene and set the root pane
        Scene scene = new Scene(root, 400, 300);

        // Set CSS styling for the text
        text.getStyleClass().add("my-shelfie-text");

        // Set the scene to the primary stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
