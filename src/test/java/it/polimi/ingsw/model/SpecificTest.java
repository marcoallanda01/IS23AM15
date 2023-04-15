package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpecificTest {

    @Test
    void testGetPatternFunction() {
        List<List<List<Boolean>>> masks = new ArrayList<>();
        masks.add(new ArrayList<>());
        for (int i = 0; i < 2; i++) {
            masks.get(0).add(new ArrayList<>());
            for (int j = 0; j < 2; j++) {
                masks.get(0).get(i).add(false);
            }
        }
        // one of two
        masks.get(0).get(0).set(0, true);
        masks.get(0).get(0).set(1, true);
        // two of two
        masks.get(0).get(1).set(0, true);
        masks.get(0).get(1).set(1, true);

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

        String name = "4squares";
        Pattern pattern1a = new Specific(name, masks, 4, false, 1, 1);
        assertEquals(1, pattern1a.getPatternFunction().apply(myBookshelf));
        Pattern pattern1b = new Specific(name, masks, 5, false, 1, 1);
        assertEquals(0, pattern1b.getPatternFunction().apply(myBookshelf));

        String name2 = "6squaresWith2Types";
        Pattern pattern2a = new Specific(name2, masks, 6, false, 1, 2);
        assertEquals(1, pattern2a.getPatternFunction().apply(myBookshelf));
        Pattern pattern2b = new Specific(name2, masks, 7, false, 1, 2);
        assertEquals(0, pattern2b.getPatternFunction().apply(myBookshelf));

        List<List<List<Boolean>>> Xmasks = new ArrayList<>();
        Xmasks.add(new ArrayList<>());
        for (int i = 0; i < 3; i++) {
            Xmasks.get(0).add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                Xmasks.get(0).get(i).add(false);
            }
        }
        Xmasks.get(0).get(0).set(0, true);
        Xmasks.get(0).get(0).set(1, false);
        Xmasks.get(0).get(0).set(2, true);

        Xmasks.get(0).get(1).set(0, false);
        Xmasks.get(0).get(1).set(1, true);
        Xmasks.get(0).get(1).set(2, false);

        Xmasks.get(0).get(2).set(0, true);
        Xmasks.get(0).get(2).set(1, false);
        Xmasks.get(0).get(2).set(2, true);

        String name3 = "2X";
        Pattern pattern3a = new Specific(name3, Xmasks, 2, false, 1, 1);
        assertEquals(1, pattern3a.getPatternFunction().apply(myBookshelf));
        Pattern pattern3b = new Specific(name3, Xmasks, 4, false, 1, 1);
        assertEquals(0, pattern3b.getPatternFunction().apply(myBookshelf));
        String name4 = "4With2Types";
        Pattern pattern4a = new Specific(name4, Xmasks, 4, false, 1, 2);
        assertEquals(1, pattern4a.getPatternFunction().apply(myBookshelf));
        Pattern pattern4b = new Specific(name4, Xmasks, 5, false, 1, 2);
        assertEquals(0, pattern4b.getPatternFunction().apply(myBookshelf));

        List<List<List<Boolean>>> Cornermasks = new ArrayList<>();
        Cornermasks.add(new ArrayList<>());
        for (int i = 0; i < 5; i++) {
            Cornermasks.get(0).add(new ArrayList<>());
            for (int j = 0; j < 6; j++) {
                Cornermasks.get(0).get(i).add(false);
            }
        }
        Cornermasks.get(0).get(0).set(0, true);
        Cornermasks.get(0).get(0).set(5, true);
        Cornermasks.get(0).get(4).set(0, true);
        Cornermasks.get(0).get(4).set(5, true);


        String name5 = "Corners";
        Pattern pattern5a = new Specific(name2, Cornermasks, 1, false, 1, 1);
        assertEquals(1, pattern5a.getPatternFunction().apply(myBookshelf));
        Pattern pattern5b = new Specific(name2, Cornermasks, 2, false, 1, 1);
        assertEquals(0, pattern5b.getPatternFunction().apply(myBookshelf));

        List<List<List<Boolean>>> Couplermasks = new ArrayList<>();
        Couplermasks.add(new ArrayList<>());
        for (int i = 0; i < 1; i++) {
            Couplermasks.get(0).add(new ArrayList<>());
            for (int j = 0; j < 2; j++) {
                Couplermasks.get(0).get(i).add(false);
            }
        }
        Couplermasks.get(0).get(0).set(0, true);
        Couplermasks.get(0).get(0).set(1, true);
        Couplermasks.add(new ArrayList<>());
        for (int i = 0; i < 2; i++) {
            Couplermasks.get(1).add(new ArrayList<>());
            for (int j = 0; j < 1; j++) {
                Couplermasks.get(1).get(i).add(false);
            }
        }
        Couplermasks.get(1).get(0).set(0, true);
        Couplermasks.get(1).get(1).set(0, true);

        String name7 = "Couples";
        Pattern pattern7a = new Specific(name7, Couplermasks, 14, false, 1, 1);
        assertEquals(1, pattern7a.getPatternFunction().apply(myBookshelf));
        Pattern pattern7b = new Specific(name7, Couplermasks, 15, false, 1, 1);
        assertEquals(0, pattern7b.getPatternFunction().apply(myBookshelf));
        // you can divide the bookshelf in 15 groups of 2 (any color)
        Pattern pattern7c = new Specific(name7, Couplermasks, 15, false, 1, 6);
        assertEquals(1, pattern7c.getPatternFunction().apply(myBookshelf));
        Pattern pattern7d = new Specific(name7, Couplermasks, 16, false, 1, 6);
        assertEquals(0, pattern7d.getPatternFunction().apply(myBookshelf));
    }
}