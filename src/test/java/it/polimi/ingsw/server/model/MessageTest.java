package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.chat.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void getContent() {
        Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
        assertEquals(m.getContent(), m.getContent());
    }

    @Test
    void getSenderName() {
        Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
        assertEquals(m.getSenderName(), m.getSender().getUserName());
    }

    @Test
    void getDate() {
        Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
        assertEquals(m.getDate(), m.getDate().toString());
    }
}