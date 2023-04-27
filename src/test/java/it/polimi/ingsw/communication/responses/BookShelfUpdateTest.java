package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookShelfUpdateTest {

    @Test
    void fromJson() {
        String player = "test";
        Set<Tile> tiles = new HashSet<>();
        tiles.add(new Tile(3, 4, TileType.BOOK));
        tiles.add(new Tile(4, 1, TileType.CAT));
        tiles.add(new Tile(3, 1, TileType.TROPHY));
        BookShelfUpdate bsu = new BookShelfUpdate(player, tiles);
        System.out.println(bsu.toJson());
        System.out.println(BookShelfUpdate.fromJson(bsu.toJson()).get().toJson());
        System.out.println(
                BookShelfUpdate.fromJson("{\"name\":\"BookShelfUpdate\"," +
                        "\"player\":\"test\"," +
                        "\"tiles\":[" +
                        "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                        "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                        "{\"x\":3,\"y\":1,\"type\":\"TROPHY\"}" +
                        "]}").get().toJson()
        );


        assertEquals(Optional.of(bsu), BookShelfUpdate.fromJson("{\"name\":\"BookShelfUpdate\"," +
                "\"player\":\"test\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":1,\"type\":\"TROPHY\"}" +
                "]}"));

        assertNotEquals(Optional.of(bsu), BookShelfUpdate.fromJson("{\"name\":\"BookShelfUpdate\"," +
                "\"player\":\"boh\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":1,\"type\":\"TROPHY\"}" +
                "]}"));

        assertNotEquals(Optional.of(bsu), BookShelfUpdate.fromJson("{\"name\":\"BookShelfUpdate\"," +
                "\"player\":\"test\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));

        assertNotEquals(Optional.of(bsu), BookShelfUpdate.fromJson("{\"name\":\"LoadedGamePlayers\"," +
                "\"player\":\"test\"," +
                "\"tiles\":[" +
                "{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));


        assertEquals(Optional.empty(), BookShelfUpdate.fromJson("{\"name\":\"BookShelfUpdate\"}"));
        assertEquals(Optional.empty(), BookShelfUpdate.fromJson("kjdsvaskd"));
        assertEquals(Optional.empty(), BookShelfUpdate.fromJson("{\"answer\":true}"));
        assertEquals(Optional.empty(), BookShelfUpdate.fromJson("{\"tiles\":[\"Come\",\"Ciao\",\"Stai\"]}"));
        assertEquals(Optional.empty(), BookShelfUpdate.fromJson("{\"player\":\"test\"," +
                "\"tiles\":[{\"x\":3,\"y\":4,\"type\":\"BOOK\"}," +
                "{\"x\":4,\"y\":1,\"type\":\"CAT\"}," +
                "{\"x\":3,\"y\":0,\"type\":\"TROPHY\"}" +
                "]}"));

        assertEquals(Optional.empty(), BookShelfUpdate.fromJson("{\"name\":\"BooleanResponse\", \"result\":true}"));
    }
}