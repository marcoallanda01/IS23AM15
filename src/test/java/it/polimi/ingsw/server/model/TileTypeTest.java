package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileTypeTest {

    @Test
    void testGetRandomTileType() {
        TileType randomTileType = TileType.getRandomTileType();
        assertNotNull(randomTileType);
    }

    @Test
    void testGetNumberOfTilesPerType() {
        TileType tileType = TileType.CAT;
        int numberOfTiles = tileType.getNumberOfTilesPerType();
        assertEquals(22, numberOfTiles);
    }

    @Test
    void testGetSymbol() {
        assertEquals("🐱", TileType.CAT.getSymbol());
        assertEquals("📚", TileType.BOOK.getSymbol());
        assertEquals("🎲", TileType.GAME.getSymbol());
        assertEquals("🖼️", TileType.FRAME.getSymbol());
        assertEquals("🏆", TileType.TROPHY.getSymbol());
        assertEquals("🌱", TileType.PLANT.getSymbol());
    }

    @Test
    void testTileTypeFromName_existingName() {
        String name = "PLANT";
        TileType tileType = TileType.tileTypeFromName(name);
        assertNotNull(tileType);
        assertEquals(TileType.PLANT, tileType);
    }

    @Test
    void testTileTypeFromName_nonExistingName() {
        String name = "INVALID_TYPE";
        TileType tileType = TileType.tileTypeFromName(name);
        assertNull(tileType);
    }

    @Test
    void testValues() {
        TileType[] values = TileType.values();
        assertNotNull(values);
        assertEquals(6, values.length);
        assertEquals(TileType.CAT, values[0]);
        assertEquals(TileType.BOOK, values[1]);
        assertEquals(TileType.GAME, values[2]);
        assertEquals(TileType.FRAME, values[3]);
        assertEquals(TileType.TROPHY, values[4]);
        assertEquals(TileType.PLANT, values[5]);
    }

    @Test
    void testValueOf_existingName() {
        String name = "BOOK";
        TileType tileType = TileType.valueOf(name);
        assertNotNull(tileType);
        assertEquals(TileType.BOOK, tileType);
    }

    @Test
    void testValueOf_nonExistingName() {
        String name = "INVALID_TYPE";
        assertThrows(IllegalArgumentException.class, () -> TileType.valueOf(name));
    }
}