package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void getContent() {
    }

    @Test
    void getSenderName() {
    }

    @Test
    void getDate() {
        Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
        System.out.println(m.getDate());
        assertEquals(m.getDate(), m.date.toString());
    }
}