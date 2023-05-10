package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.JsonString;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FirstJoinResponseTest {

    @Test
    void fromJson() throws Exception {
        JsonString json1 = new JsonString("{\"name\":\n\"FirstJoinResponse\", \"result\":true\n}\n");
        JsonString json2 = new JsonString("{\"name\":\"FirstJoinResponse\", \"result\":false}");
        assertEquals(Optional.of(new FirstJoinResponse(true)), FirstJoinResponse.fromJson(json1.getJson()));
        assertEquals(Optional.of(new FirstJoinResponse(false)), FirstJoinResponse.fromJson(json2.getJson()));
        assertEquals(Optional.empty(), FirstJoinResponse.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), FirstJoinResponse.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), FirstJoinResponse.fromJson("{\"result\":true}"));
        assertEquals(Optional.empty(), FirstJoinResponse.fromJson("{\"name\":\"Response\", \"answer\":true}"));
    }
}