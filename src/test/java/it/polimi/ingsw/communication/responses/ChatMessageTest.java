package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.server.model.chat.Message;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

class ChatMessageTest {

    @Test
    void fromJson() {
        Message m = new Message(new Player("test"), "ciao");
        ChatMessage cm = new ChatMessage(m);
        assertEquals(cm, cm);
        ChatMessage cm2 = new ChatMessage("test", m.getDate(), "ciao");
        assertEquals(cm, cm2);
        String json1 = "{\"name\":\"ChatMessage\", \"message\":\"ciao\"," +
                "\"sender\":\"test\",\"date\":\"2023-05-01T11:32:45.246152\"}";
        String json2 = "{\"name\":\"ChatMessage\", \"message\":\"ciao\"," +
                "\"sender\":\"test\", \"date\":\"2023-05-01T12:32:45.246152\"}";

        System.out.println(json1);
        assertEquals(ChatMessage.fromJson(json1), ChatMessage.fromJson(json1));
        // date
        assertNotEquals(ChatMessage.fromJson(json1), ChatMessage.fromJson(json2));
        // message
        json2 = "{\"name\":\"ChatMessage\", \"message\":\"boh\"," +
                "\"sender\":\"test\", \"date\":\"2023-05-01T11:32:45.246152\"}";
        assertNotEquals(ChatMessage.fromJson(json1), ChatMessage.fromJson(json2));
        // sender
        json2 = "{\"name\":\"ChatMessage\", \"message\":\"test\"," +
                "\"sender\":\"boh\", \"date\":\"2023-05-01T11:32:45.246152\"}";
        assertNotEquals(ChatMessage.fromJson(json1), ChatMessage.fromJson(json2));


        String json = "{\"name\":\"boh\",\"message\":null,\"sender\":\"Alice\",\"date\":\"2023-05-01\"}";
        Optional<ChatMessage> result = ChatMessage.fromJson(json);
        assertTrue(result.isEmpty());

        json = "{\"name\":\"ChatMessage\",\"message\":null,\"sender\":\"Alice\",\"date\":\"2023-05-01\"}";
        result = ChatMessage.fromJson(json);
        assertTrue(result.isEmpty());

        json = "{\"name\":\"ChatMessage\",\"message\":\"Hello\",\"sender\":null,\"date\":\"2023-05-01\"}";
        result = ChatMessage.fromJson(json);
        assertTrue(result.isEmpty());

        json = "{\"name\":\"ChatMessage\",\"message\":\"Hello\",\"sender\":\"Alice\",\"date\":null}";
        result = ChatMessage.fromJson(json);
        assertTrue(result.isEmpty());

        json = "dsok";
        result = ChatMessage.fromJson(json);
        assertTrue(result.isEmpty());
    }
}