package it.polimi.ingsw.communication.responses;

import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TilesPickedTest {

    @Test
    void fromJson() {
        // Valid JSON with correct fields and values
        String validJson = "{\"name\":\"TilesPicked\"," +
                "\"player\":\"test\"," +
                "\"tiles\":[\"CAT\",\"BOOK\",\"GAME\",\"CAT\"]}";

        // Valid JSON with missing name field
        String missingNameJson = "{\"player\":\"test\"," +
                "\"tiles\":[\"CAT\",\"BOOK\",\"GAME\"]}";

        // Valid JSON with missing player field
        String missingPlayerJson = "{\"name\":\"TilesPicked\"," +
                "\"tiles\":[\"CAT\",\"BOOK\",\"GAME\"]}";

        // Valid JSON with missing tiles field
        String missingTilesJson = "{\"name\":\"TilesPicked\"," +
                "\"player\":\"test\"}";

        // Valid JSON with incorrect tiles field type
        String incorrectTilesTypeJson = "{\"name\":\"TilesPicked\"," +
                "\"player\":\"test\"," +
                "\"tiles\":\"invalid\"}";

        // Valid JSON with empty tiles list
        String emptyTilesListJson = "{\"name\":\"TilesPicked\"," +
                "\"player\":\"test\"," +
                "\"tiles\":[]}";

        // Invalid JSON with random characters
        String randomJson = "{\"name\":\"TilesPicked\"," +
                "\"player\":%#@$%#," +
                "\"tiles\":[\"CAT\",\"BOOK\",\"GAME\"]}";

        // Test valid JSON
        Optional<TilesPicked> result = TilesPicked.fromJson(validJson);
        List<TileType> tiles = new ArrayList<>();
        tiles.add(TileType.CAT);
        tiles.add(TileType.BOOK);
        tiles.add(TileType.GAME);
        tiles.add(TileType.CAT);
        assertTrue(result.isPresent());
        assertEquals(new TilesPicked("test", tiles), result.get());
        assertEquals("test", result.get().player);
        assertEquals(4, result.get().tiles.size());
        assertTrue(result.get().tiles.contains(TileType.CAT));
        assertTrue(result.get().tiles.contains(TileType.BOOK));
        assertTrue(result.get().tiles.contains(TileType.GAME));

        // Test JSON with missing name field
        result = TilesPicked.fromJson(missingNameJson);
        assertEquals(Optional.empty(), result);

        // Test JSON with missing player field
        result = TilesPicked.fromJson(missingPlayerJson);
        assertEquals(Optional.empty(), result);

        // Test JSON with missing tiles field
        result = TilesPicked.fromJson(missingTilesJson);
        assertEquals(Optional.empty(), result);

        // Test JSON with incorrect tiles field type
        result = TilesPicked.fromJson(incorrectTilesTypeJson);
        assertEquals(Optional.empty(), result);

        // Test JSON with empty tiles list
        result = TilesPicked.fromJson(emptyTilesListJson);
        assertTrue(result.isPresent());
        assertEquals("test", result.get().player);
        assertEquals(0, result.get().tiles.size());

        // Test invalid JSON with random characters
        result = TilesPicked.fromJson(randomJson);
        assertEquals(Optional.empty(), result);
    }

}