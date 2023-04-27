package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.communication.responses.BoardUpdate;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PickTilesCommandTest {

    @Test
    void fromJson() {
        Set<Tile> tiles = new HashSet<>();
        tiles.add(new Tile(3, 4, TileType.BOOK));
        tiles.add(new Tile(4, 1, TileType.CAT));
        tiles.add(new Tile(3, 1, TileType.TROPHY));
        PickTilesCommand ptc = new PickTilesCommand("test",tiles);
        System.out.println(ptc.toJson());
        System.out.println(PickTilesCommand.fromJson(ptc.toJson()).get().toJson());
        System.out.println(
                PickTilesCommand.fromJson("{\"name\":\"PickTilesCommand\"," +
                        "\"id\":\"test\"," +
                        "\"tiles\":[" +
                        "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                        "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                        "{\"x\":3,\"y\":1,\"type\":\"TROPHY\"}" +
                        "]}").get().toJson()
        );
        assertEquals(Optional.of(ptc), PickTilesCommand.fromJson("{\"name\":\"PickTilesCommand\"," +
                "\"id\":\"test\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":1,\"type\":\"TROPHY\"}" +
                "]}"));

        assertNotEquals(Optional.of(ptc), PickTilesCommand.fromJson("{\"name\":\"PickTilesCommand\"," +
                "\"id\":\"test\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));

        assertNotEquals(Optional.of(ptc), PickTilesCommand.fromJson("{\"name\":\"PickTilesCommand\"," +
                "\"id\":\"test\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));


        assertEquals(Optional.empty(), PickTilesCommand.fromJson("{\"name\":\"PickTilesCommand\"}"));
        assertEquals(Optional.empty(), PickTilesCommand.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), PickTilesCommand.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), PickTilesCommand.fromJson("\"id\":\"test\"" +
                "{\"tiles\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), PickTilesCommand.fromJson("\"id\":\"test\"," +
                "{\"tiles\":[{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));

        assertEquals(Optional.empty(), PickTilesCommand.fromJson("{\"name\":\"Disconnect\",\"id\":\"test\"}"));
    }
}