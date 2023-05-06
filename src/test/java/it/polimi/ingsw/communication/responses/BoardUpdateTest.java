package it.polimi.ingsw.communication.responses;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BoardUpdateTest {

    @Test
    void fromJson() {
        Set<Tile> tiles = new HashSet<>();
        tiles.add(new Tile(3, 4, TileType.BOOK));
        tiles.add(new Tile(4, 1, TileType.CAT));
        tiles.add(new Tile(3, 1, TileType.TROPHY));
        BoardUpdate bu = new BoardUpdate(tiles,false);
        System.out.println(bu.toJson());
        System.out.println(BoardUpdate.fromJson(bu.toJson()).get().toJson());
        System.out.println(
                BoardUpdate.fromJson("{\"name\":\"BoardUpdate\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":1,\"type\":\"TROPHY\"}" +
                "]}").get().toJson()
        );
        assertEquals(Optional.of(bu), BoardUpdate.fromJson("{\"name\":\"BoardUpdate\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":1,\"type\":\"TROPHY\"}" +
                "]}"));

        assertNotEquals(Optional.of(bu), BoardUpdate.fromJson("{\"name\":\"BoardUpdate\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));

        assertNotEquals(Optional.of(bu), BoardUpdate.fromJson("{\"name\":\"LoadedGamePlayers\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));


        assertEquals(Optional.empty(), BoardUpdate.fromJson("{\"name\":\"BoardUpdate\"}"));
        assertEquals(Optional.empty(), BoardUpdate.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), BoardUpdate.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), BoardUpdate.fromJson("{\"tiles\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), BoardUpdate.fromJson("{\"tiles\":[{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));

        assertEquals(Optional.empty(), BoardUpdate.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}