package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void getContent() {
        Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
        assertEquals(m.getContent(), m.content);
    }

    @Test
    void getSenderName() {
        Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
        assertEquals(m.getSenderName(), m.sender.getUserName());
    }

    @Test
    void getDate() {
        Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
        assertEquals(m.getDate(), m.date.toString());
    }
}