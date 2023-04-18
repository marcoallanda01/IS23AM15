package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdjacentTest {

    @Test
    void testGetPatternFunction() {
        BookShelf bookShelf = new BookShelf();
        // TCCTT
        // CCCCT
        // CCCCT
        // CCCCC
        // CCTCC
        // TTTTT
        List<Tile> one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        String name = "6+";
        Pattern pattern1a = new Adjacent(name,6, 1, 8);
        assertEquals(16, pattern1a.getPatternFunction().apply(myBookshelf));

        String name2 = "5";
        Pattern pattern2a = new Adjacent(name2,5, 1, 5);
        assertEquals(0, pattern1a.getPatternFunction().apply(myBookshelf));

        String name3 = "4";
        Pattern pattern3a = new Adjacent(name3,4, 1, 3);
        assertEquals(3, pattern3a.getPatternFunction().apply(myBookshelf));

        String name4 = "3";
        Pattern pattern4a = new Adjacent(name3,3, 1, 3);
        assertEquals(0, pattern4a.getPatternFunction().apply(myBookshelf));
    }
}