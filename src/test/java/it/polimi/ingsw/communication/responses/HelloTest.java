package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.communication.responses.Hello;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @Test
    void fromJson() {
        boolean lobbyReady = false;
        String idFirstPlayer = "test";

        String json = "{\"name\":\"Hello\", \"lobbyReady\":"+lobbyReady+"," + "\"firstPlayerId\":\""+idFirstPlayer+"\"}";
        assertEquals(Optional.of(new Hello(idFirstPlayer)), Hello.fromJson(json));

        lobbyReady = true;
        json = "{\"name\":\"Hello\", \"lobbyReady\":"+lobbyReady+"," + "\"firstPlayerId\":\""+idFirstPlayer+"\"}";
        assertNotEquals(Optional.of(new Hello(lobbyReady)), Hello.fromJson(json));
        idFirstPlayer = "NoFirst";
        json = "{\"name\":\"Hello\", \"lobbyReady\":"+lobbyReady+"," + "\"firstPlayerId\":\""+idFirstPlayer+"\"}";
        assertEquals(Optional.of(new Hello(lobbyReady)), Hello.fromJson(json));


        json = "{\"name\":\"Hello\", \"lobbyReady\":"+lobbyReady+"," + "\"firstPlayerId\":\""+idFirstPlayer+"\"}";
        assertNotEquals(Optional.of(new Hello(false)), Hello.fromJson(json));

        assertEquals(Optional.empty(),Hello.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),Hello.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(),Hello.fromJson("{\"idFirstPlayer\":\"test\"}"));
        assertEquals(Optional.empty(),Hello.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}