package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatControllerTest {

    List<String> playersNames = new ArrayList<>(List.of("p1", "p2", "p3"));


    @Test
    void getPlayerMessages() throws PlayerNotFoundException {
        Game game;
        PushNotificationController pnc;
        pnc = new PushNotificationController(new ArrayList<>());
        game = new Game(pnc);
        game.setGame(playersNames, false, null);
        ChatController chatController = new ChatController(game);
        assertNotNull(chatController.getPlayerMessages("p3"));
        assertThrows(PlayerNotFoundException.class, ()->chatController.getPlayerMessages("ntExists"));
    }

    @Test
    void sendMessages() throws PlayerNotFoundException {
        Game game;
        PushNotificationController pnc;
        pnc = new PushNotificationController(new ArrayList<>());
        game = new Game(pnc);
        game.setGame(playersNames, false, null);
        ChatController chatController = new ChatController(game);
        chatController.sendMessage("p1", "p2", "ciao p2");
        chatController.sendMessage("p2", "p1", "ciao p1");
        chatController.sendMessage("p3", "p1", "ciao p1");
        chatController.sendMessage("p1", "ciao a tutti");
        assertThrows(PlayerNotFoundException.class, ()->chatController.sendMessage("p3", "nonPresente", "ciao p1"));
        assertThrows(PlayerNotFoundException.class, ()->chatController.sendMessage("nonPresente",  "p1", "ciao p1"));
        assertEquals(chatController.getPlayerMessages("p1").size(), 4);
        assertEquals(chatController.getPlayerMessages("p2").size(), 3);
        assertEquals(chatController.getPlayerMessages("p3").size(), 2);
        assertThrows(PlayerNotFoundException.class, ()->chatController.sendMessage("p1", "p5", "ciao p5"));
        assertThrows(PlayerNotFoundException.class, ()->chatController.sendMessage("p5", "p1", "ciao p5"));
    }
}