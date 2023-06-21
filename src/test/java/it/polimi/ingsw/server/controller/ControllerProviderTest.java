package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControllerProviderTest {

    ControllerProvider cp;
    @BeforeEach
    void setUp(){
        PushNotificationController pnc = new PushNotificationController(new ArrayList<>());
        cp = new ControllerProvider(new Game(pnc), "test", pnc);
    }
    @Test
    void getChatController() {
        assertNotNull(cp.getChatController());
    }

    @Test
    void getPlayController() {
        assertNotNull(cp.getPlayController());
    }
}