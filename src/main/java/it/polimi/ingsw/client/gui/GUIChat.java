package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Client;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GUIChat extends GUIState {
    Map<String, Map<String, String>> chat;
    List<String> players;

    public GUIChat(GUIApplication guiApplication, Map<String, Map<String, String>> chat, List<String> players) {
        super(guiApplication);
        this.chat = chat;
        this.players = players;
        createUI();
    }

    public void createUI() {
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));
        root.setAlignment(javafx.geometry.Pos.CENTER);

        ListView<HBox> chatList = new ListView<>();

        chatList.setPrefSize(250, 300);

        LinkedHashMap<String, Map<String, String>> sortedChat = chat.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(key -> LocalDateTime.parse(key, DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        for (String dateTimeString : sortedChat.keySet()) {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String time = dateTime.format(hourFormatter);
            String nickname = chat.get(dateTimeString).get("nickname");
            String message = chat.get(dateTimeString).get("message");
            HBox hBox = new HBox();
            Label timeName = new Label(time + " " + nickname + ": ");
            timeName.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
            hBox.getChildren().add(timeName);
            Label messageLabel = new Label(message);
            hBox.getChildren().add(messageLabel);
            chatList.getItems().add(hBox);
        }

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Select a player");
        comboBox.getItems().add("All");
        this.players.remove(Client.getInstance().getNickname());
        comboBox.getItems().addAll(this.players);
        comboBox.setValue("All");

        HBox messageBox = new HBox(10);

        TextField messageField = new TextField();
        messageField.setPrefWidth(250);
        messageField.setPromptText("Type your message here");
        messageField.setOnAction(e -> {
            String message = messageField.getText();
            if (!message.equals("")) {
                if (comboBox.getValue() != null) {
                    if (comboBox.getValue().equals("All")) {
                        Client.getInstance().getClientController().sendChatMessage(message);
                    } else {
                        Client.getInstance().getClientController().sendChatMessage(comboBox.getValue(), message);
                    }
                }
                messageField.clear();
            }
        });

        messageBox.getChildren().addAll(comboBox, messageField);

        root.getChildren().addAll(chatList, messageBox);

        Scene scene = new Scene(root, 350, 300);
        Platform.runLater(() -> guiApplication.showChat(scene));
    }

}
