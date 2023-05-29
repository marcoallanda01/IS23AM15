package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.chat.Chat;
import it.polimi.ingsw.server.model.chat.Message;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {
    Player p1 = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");
    Player p4 = new Player("p4");
    Message m1 = new Message(p1, p2, "ciao p2");
    Message m2 = new Message(p2, p1, "ciao p1");
    Message m3 = new Message(p3, p1, "ciao p1");
    Message m4 = new Message(p1, p4, "ciao p4");
    Message m5 = new Message(p1, "ciao a tutti");
    Message m6 = new Message(p4, p1, "ciao p1");
    List<Player> players = new ArrayList<>(List.of(p1, p2, p3));
    @Test
    void getMessages() throws PlayerNotFoundException {
        Chat c = new Chat(players);
        c.addMessage(m1);
        c.addMessage(m2);
        c.addMessage(m3);
        c.addMessage(m5);
        assertThrows(PlayerNotFoundException.class, () -> c.addMessage(m4));
        assertThrows(PlayerNotFoundException.class, () -> c.addMessage(m6));
        assertEquals(c.getMessages(p1), List.of(m5, m3, m2, m1));
        assertEquals(c.getMessages(p2), List.of(m5, m2, m1));
    }

    @Test
    void addMessage() throws PlayerNotFoundException {
        Chat c = new Chat(players);
        assertEquals(c.getMessages(p1), new ArrayList<>());
        assertEquals(c.getMessages(p2), new ArrayList<>());
        assertEquals(c.getMessages(p3), new ArrayList<>());
        c.addMessage(m1);
        c.addMessage(m2);
        c.addMessage(m3);
        assertEquals(c.getMessages(p1), List.of(m3, m2,m1));
        assertEquals(c.getMessages(p2), List.of(m2,m1));
    }
}