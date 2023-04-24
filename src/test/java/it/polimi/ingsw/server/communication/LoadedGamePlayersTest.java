package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.communication.responses.LoadedGamePlayers;
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

        assertEquals(Optional.of(new LoadedGamePlayers(n)), LoadedGamePlayers.fromJson("{\"name\":\"LoadedGamePlayers\"," +
                "\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.of(new LoadedGamePlayers(new HashSet<String>())), LoadedGamePlayers.fromJson("{\"name\":\"LoadedGamePlayers\"," +
                "\"names\":[]}"));
        assertEquals(Optional.empty(), LoadedGamePlayers.fromJson("{\"name\":\"LoadedGamePlayers\"}"));
        assertEquals(Optional.empty(), LoadedGamePlayers.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), LoadedGamePlayers.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), LoadedGamePlayers.fromJson("{\"names\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), LoadedGamePlayers.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}