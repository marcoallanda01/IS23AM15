package it.polimi.ingsw.communication.commands;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SendMessageTest {

    @Test
    void fromJson() {
        String id = "test";

        String json = "{\"name\":\"SendMessage\",\"id\":\""+id+"\", \"message\":\"m\"}";
        System.out.println(json);
        System.out.println(new SendMessage(id, "m").toJson());
        assertEquals(Optional.of(new SendMessage(id, "m")), SendMessage.fromJson(json));
        assertNotEquals(Optional.of(new SendMessage(id, "m", "name")), SendMessage.fromJson(json));
        json = "{\"name\":\"SendMessage\",\"id\":\""+id+"\", \"message\":\"m\", \"player\":\"name\"}";
        assertEquals(Optional.of(new SendMessage(id, "m", "name")), SendMessage.fromJson(json));
        json = "{\"name\":\"SendMessage\",\"id\":\""+id+"\"}";
        assertNotEquals(Optional.of(new SendMessage("boh", "m")), SendMessage.fromJson(json));
        assertNotEquals(Optional.of(new SendMessage("test", "boh")), SendMessage.fromJson(json));

        assertEquals(Optional.empty(),SendMessage.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),SendMessage.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),SendMessage.fromJson("{\"id\":\"test\"}"));
        assertEquals(Optional.empty(),SendMessage.fromJson("{\"name\":\"SendMessage\"}"));
        assertEquals(Optional.empty(),SendMessage.fromJson("{\"name\":\"SendMessage\", " +
                "\"id\":\"test\"}"));
        assertEquals(Optional.empty(),SendMessage.fromJson("{\"name\":\"SendMessage\", " +
                "\"message\":\"m\"}"));
        assertEquals(Optional.empty(),SendMessage.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}