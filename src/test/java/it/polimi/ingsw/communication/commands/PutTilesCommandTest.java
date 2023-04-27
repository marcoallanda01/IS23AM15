package it.polimi.ingsw.communication.commands;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PutTilesCommandTest {

    @Test
    void fromJson() {
        List<TileType> tiles = new ArrayList<>();
        tiles.add(TileType.BOOK);
        tiles.add(TileType.BOOK);
        tiles.add(TileType.CAT);
        tiles.add(TileType.TROPHY);
        int column = 3;
        PutTilesCommand ptc = new PutTilesCommand("test",tiles, 3);
        System.out.println(ptc.toJson());
        System.out.println(PutTilesCommand.fromJson(ptc.toJson()).get().toJson());
        System.out.println(
                PutTilesCommand.fromJson("{\"name\":\"PutTilesCommand\"," +
                        "\"id\":\"test\"," +
                        "\"column\":3," +
                        "\"tiles\":[\"BOOK\",\"BOOK\",\"CAT\",\"TROPHY\"]}").get().toJson()
        );
        assertEquals(Optional.of(ptc), PutTilesCommand.fromJson("{\"name\":\"PutTilesCommand\"," +
                        "\"id\":\"test\"," +
                        "\"column\":3," +
                        "\"tiles\":[\"BOOK\",\"BOOK\",\"CAT\",\"TROPHY\"]}")
        );

        assertNotEquals(Optional.of(ptc), PutTilesCommand.fromJson("{\"name\":\"PutTilesCommand\"," +
                "\"id\":\"test\"," +
                "\"column\":9," +
                "\"tiles\":[\"BOOK\",\"BOOK\",\"CAT\",\"TROPHY\"]}")
        );
        assertNotEquals(Optional.of(ptc), PutTilesCommand.fromJson("{\"name\":\"PutTilesCommand\"," +
                "\"id\":\"test\"," +
                "\"column\":9," +
                "\"tiles\":[\"CAT\",\"BOOK\",\"CAT\",\"TROPHY\"]}")
        );

        assertNotEquals(Optional.of(ptc), PutTilesCommand.fromJson("{\"name\":\"PutTilesCommand\"," +
                "\"id\":\"boh\"," +
                "\"column\":9," +
                "\"tiles\":[\"CAT\",\"BOOK\",\"CAT\",\"TROPHY\"]}")
        );

        assertEquals(Optional.empty(), PutTilesCommand.fromJson("{\"name\":\"PutTilesCommand\"}"));
        assertEquals(Optional.empty(), PutTilesCommand.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), PutTilesCommand.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), PutTilesCommand.fromJson("{\"tiles\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), PutTilesCommand.fromJson("{\"id\":\"boh\",\"tiles\":[\"CAT\",\"BOOK\",\"CAT\",\"TROPHY\"]}"));

        assertEquals(Optional.empty(), PutTilesCommand.fromJson("{\"name\":\"Disconnect\"}"));
    }
}