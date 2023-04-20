package it.polimi.ingsw.communication;

import it.polimi.ingsw.controller.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoadGameResponseTest {

    @Test
    void fromJson() {
        assertEquals(Optional.of(new LoadGameResponse()), LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\"," +
                "\"result\":true}"));
        LoadGameResponse jgre = new LoadGameResponse(new GameLoadException());
        LoadGameResponse jgra = LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false," +
                "\"error\":\"GameLoadException\"}").get();
        assertEquals(jgre , jgra);
        assertEquals(Optional.of(new LoadGameResponse(new GameNameException())),
                LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false, \"error\":\"GameNameException\"}"));
        assertEquals(Optional.of(new LoadGameResponse(new IllegalLobbyException())),
                LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false, \"error\":\"IllegalLobbyException\"}"));

        assertNotEquals(Optional.of(new LoadGameResponse(new IllegalLobbyException())),
                LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":false, \"error\":\"GameNameException\"}"));


        assertEquals(Optional.empty(),
                LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\",\"result\":true, \"error\":\"IllegalLobbyException\"}"));
        assertEquals(Optional.empty(), LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\"," +
                "\"result\":false}"));
        LoadGameResponse lgr = LoadGameResponse.fromJson("{\"name\":\"LoadGameResponse\"}").get();
        assertEquals(Optional.empty(),LoadGameResponse.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(),LoadGameResponse.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}