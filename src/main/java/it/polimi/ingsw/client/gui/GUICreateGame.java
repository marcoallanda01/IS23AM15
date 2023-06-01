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


/**
 * Generates the GUI for creating a game.
 */
public class GUICreateGame extends GUIState {

    /**
     * Constructs a new GUICreateGame instance.
     *
     * @param guiApplication The GUI application to associate with.
     */
    public GUICreateGame(GUIApplication guiApplication) {
        super(guiApplication);
        createUI();
    }

    /**
     * Creates the game creation form UI.
     */
    private void createUI() {
        VBox root = createRootPane();
        Scene scene = new Scene(root, 800, 700);
        Platform.runLater(() -> guiApplication.transitionToScene(scene));
    }

    /**
     * Creates the root pane for the game creation form.
     *
     * @return The created VBox root pane.
     */
    private VBox createRootPane() {
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        ToggleGroup playerGroup = createPlayerForm(root);
        ToggleGroup rulesGroup = createRulesForm(root);
        createSubmitButton(root, playerGroup, rulesGroup);

        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F4F4F4;");

        return root;
    }

    /**
     * Creates the form for selecting the number of players.
     *
     * @param root The root pane to add the form components to.
     * @return The created ToggleGroup for the player selection.
     */
    private ToggleGroup createPlayerForm(VBox root) {
        Label numOfPlayersLabel = createLabel("Number of Players:");
        ToggleGroup playerGroup = new ToggleGroup();
        RadioButton player2 = createRadioButton("2", playerGroup);
        RadioButton player3 = createRadioButton("3", playerGroup);
        RadioButton player4 = createRadioButton("4", playerGroup);

        root.getChildren().addAll(numOfPlayersLabel, player2, player3, player4);

        return playerGroup;
    }

    /**
     * Creates the form for selecting the rules.
     *
     * @param root The root pane to add the form components to.
     * @return The created ToggleGroup for the rules selection.
     */
    private ToggleGroup createRulesForm(VBox root) {
        Label rulesLabel = createLabel("Rules:");
        ToggleGroup rulesGroup = new ToggleGroup();
        RadioButton easyRules = createRadioButton("Easy", rulesGroup);
        RadioButton advancedRules = createRadioButton("Advanced", rulesGroup);

        root.getChildren().addAll(rulesLabel, easyRules, advancedRules);

        return rulesGroup;
    }

    /**
     * Creates the submit button for creating the game.
     *
     * @param root       The root pane to add the submit button to.
     * @param playerGroup The ToggleGroup for player selection.
     * @param rulesGroup The ToggleGroup for rules selection.
     */
    private void createSubmitButton(VBox root, ToggleGroup playerGroup, ToggleGroup rulesGroup) {
        Button submitButton = new Button("Create Game");
        submitButton.setOnAction(event -> handleCreateGame(playerGroup, rulesGroup));

        root.getChildren().add(submitButton);
    }

    /**
     * Handles the creation of the game when the submit button is clicked.
     *
     * @param playerGroup The ToggleGroup for player selection.
     * @param rulesGroup The ToggleGroup for rules selection.
     */
    private void handleCreateGame(ToggleGroup playerGroup, ToggleGroup rulesGroup) {
        int numOfPlayers = getSelectedNumOfPlayers(playerGroup);
        boolean isEasyRules = isEasyRulesSelected(rulesGroup);

        Client.getInstance().getClientController().createGame(numOfPlayers, isEasyRules);
    }

    /**
     * Retrieves the selected number of players from the toggle group.
     *
     * @param playerGroup The toggle group for selecting the number of players.
     * @return The selected number of players.
     */
    private int getSelectedNumOfPlayers(ToggleGroup playerGroup) {
        RadioButton selectedRadioButton = (RadioButton) playerGroup.getSelectedToggle();
        String selectedNumOfPlayers = selectedRadioButton.getText();
        return Integer.parseInt(selectedNumOfPlayers);
    }

    /**
     * Checks if the "Easy" rules option is selected.
     *
     * @param rulesGroup The toggle group for selecting the rules.
     * @return {@code true} if the "Easy" rules option is selected, {@code false} otherwise.
     */
    private boolean isEasyRulesSelected(ToggleGroup rulesGroup) {
        RadioButton selectedRadioButton = (RadioButton) rulesGroup.getSelectedToggle();
        return selectedRadioButton.getText().equals("Easy");
    }

    /**
     * Creates a label with the specified text.
     *
     * @param text The text for the label.
     * @return The created Label.
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        return label;
    }

    /**
     * Creates a radio button with the specified text and toggle group.
     *
     * @param text  The text for the radio button.
     * @param group The toggle group for the radio button.
     * @return The created RadioButton.
     */
    private RadioButton createRadioButton(String text, ToggleGroup group) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setFont(Font.font("Arial", 12));
        return radioButton;
    }
}