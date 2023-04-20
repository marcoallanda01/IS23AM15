package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.controller.GameLoadException;
import it.polimi.ingsw.server.controller.GameNameException;
import it.polimi.ingsw.server.controller.IllegalLobbyException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoadGameResponseTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.LoadGameResponse()), it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\"," +
                "\"result\":true}"));
        it.polimi.ingsw.server.communication.LoadGameResponse jgre = new it.polimi.ingsw.server.communication.LoadGameResponse(new GameLoadException());
        it.polimi.ingsw.server.communication.LoadGameResponse jgra = it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false," +
                "\"error\":\"GameLoadException\"}").get();
        assertEquals(jgre , jgra);
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.LoadGameResponse(new GameNameException())),
                it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false, \"error\":\"GameNameException\"}"));
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.LoadGameResponse(new IllegalLobbyException())),
                it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false, \"error\":\"IllegalLobbyException\"}"));

        assertNotEquals(Optional.of(new it.polimi.ingsw.server.communication.LoadGameResponse(new IllegalLobbyException())),
                it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false, \"error\":\"GameNameException\"}"));


        assertEquals(Optional.empty(),
                it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":true, \"error\":\"IllegalLobbyException\"}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\"," +
                "\"result\":false}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadGameResponse.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}