package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GUICreateGame extends GUIState {
    public GUICreateGame(GUIApplication guiApplication) {
        super(guiApplication);
        createGameForm();
    }

    private void createGameForm() {
        // Create labels and radio buttons for the form
        Label numOfPlayersLabel = createLabel("Number of Players:");
        ToggleGroup playerGroup = new ToggleGroup();
        RadioButton player2 = createRadioButton("2", playerGroup);
        RadioButton player3 = createRadioButton("3", playerGroup);
        RadioButton player4 = createRadioButton("4", playerGroup);

        Label rulesLabel = createLabel("Rules:");
        ToggleGroup rulesGroup = new ToggleGroup();
        RadioButton easyRules = createRadioButton("Easy", rulesGroup);
        RadioButton advancedRules = createRadioButton("Advanced", rulesGroup);

        // Create the submit button
        Button submitButton = new Button("Create Game");
        submitButton.setOnAction(event -> {
            // Get the selected number of players
            int numOfPlayers = getSelectedNumOfPlayers(playerGroup);

            // Get the selected rules option
            boolean isEasyRules = isEasyRulesSelected(rulesGroup);

            // Call the createGame function with the selected number of players and rules option
            Client.getInstance().getClientController().createGame(numOfPlayers, isEasyRules);
        });

        // Create the layout pane
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(numOfPlayersLabel, player2, player3, player4, rulesLabel, easyRules, advancedRules, submitButton);
        root.setAlignment(Pos.CENTER);

        // Apply inline styles
        root.setStyle("-fx-background-color: #F4F4F4;");

        // Create the scene and set the root pane
        Scene scene = new Scene(root, 400, 300);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        return label;
    }

    private RadioButton createRadioButton(String text, ToggleGroup group) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setFont(Font.font("Arial", 12));
        return radioButton;
    }

    private int getSelectedNumOfPlayers(ToggleGroup playerGroup) {
        RadioButton selectedRadioButton = (RadioButton) playerGroup.getSelectedToggle();
        String selectedNumOfPlayers = selectedRadioButton.getText();
        return Integer.parseInt(selectedNumOfPlayers);
    }

    private boolean isEasyRulesSelected(ToggleGroup rulesGroup) {
        RadioButton selectedRadioButton = (RadioButton) rulesGroup.getSelectedToggle();
        return selectedRadioButton.getText().equals("Easy");
    }
}

