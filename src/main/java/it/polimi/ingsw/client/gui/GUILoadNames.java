package it.polimi.ingsw.client.gui;

import java.util.List;
import it.polimi.ingsw.client.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.util.List;

public class GUILoadNames extends GUIState{
    List<String> savedNames;

    /**
     * Constructor for GUILoadNames
     * @param guiApplication is the GUIApplication
     * @param savedNames is the list of saved names
     */
    public GUILoadNames(GUIApplication guiApplication, List<String> savedNames) {
        super(guiApplication);
        this.savedNames = savedNames;
        createUI();
    }

    /**
     * Method to create the UI
     */
    public void createUI(){
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Load Name");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label savedNamesLabel = new Label("Saved Names:");
        savedNamesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        VBox savedNamesBox = createSavedNamesBox();

        root.getChildren().addAll(titleLabel, savedNamesLabel, savedNamesBox);

        Scene scene = new Scene(root, 800, 700);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    /**
     * Method to create the VBox of saved names
     * @return the VBox of saved names
     */
    private VBox createSavedNamesBox() {
        VBox savedNamesBox = new VBox(10); // Added spacing between buttons
        savedNamesBox.setPadding(new Insets(10));
        savedNamesBox.setAlignment(Pos.CENTER);

        for (String savedName : savedNames) {
            Button savedNameButton = new Button(savedName);
            savedNameButton.setOnAction(event -> {
                Client.getInstance().getClientController().loginLoaded(savedName);
            });
            savedNamesBox.getChildren().add(savedNameButton);
        }

        return savedNamesBox;
    }
}
