package it.polimi.ingsw.client.gui;
import it.polimi.ingsw.client.Client;
import javafx.animation.FadeTransition;
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
        Scene scene = new Scene(root, 400, 300);

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
        // Create a fade transition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), primaryStage.getScene().getRoot());
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> {
            // Set the new scene after the fade transition
            primaryStage.setScene(newScene);

            // Create another fade transition to show the new scene
            FadeTransition showTransition = new FadeTransition(Duration.seconds(2), primaryStage.getScene().getRoot());
            showTransition.setFromValue(0.0);
            showTransition.setToValue(1.0);
            showTransition.play();
        });
        fadeTransition.play();
    }

}
