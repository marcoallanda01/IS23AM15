package it.polimi.ingsw.server.communication;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoadedGamePlayersTest {

    @Test
    void fromJson(){
        Set<String> n = new HashSet<>();
        n.add("Ciao");
        n.add("Come");
        n.add("Stai");

        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.LoadedGamePlayers(n)), it.polimi.ingsw.server.communication.LoadedGamePlayers.fromJson("{\"name\":\"LoadedGamePlayers\"," +
                "\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.of(new it.polimi.ingsw.server.communication.LoadedGamePlayers(new HashSet<String>())), it.polimi.ingsw.server.communication.LoadedGamePlayers.fromJson("{\"name\":\"LoadedGamePlayers\"," +
                "\"names\":[]}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadedGamePlayers.fromJson("{\"name\":\"LoadedGamePlayers\"}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadedGamePlayers.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadedGamePlayers.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadedGamePlayers.fromJson("{\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), it.polimi.ingsw.server.communication.LoadedGamePlayers.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}