package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdjacentTest {

    @Test
    void testGetPatternFunction() throws InvalidPatternParameterException {
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
        Pattern pattern1a = new Adjacent(name,6, 8);
        assertEquals(16, pattern1a.getPatternFunction().apply(myBookshelf));

        String name2 = "5";
        Pattern pattern2a = new Adjacent(name2,5, 1, 5);
        assertEquals(0, pattern2a.getPatternFunction().apply(myBookshelf));

        String name3 = "4";
        Pattern pattern3a = new Adjacent(name3,4, 1, 3);
        assertEquals(3, pattern3a.getPatternFunction().apply(myBookshelf));

        String name4 = "3";
        Pattern pattern4a = new Adjacent(name3,3, 1, 2);
        assertEquals(0, pattern4a.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void test6() throws InvalidPatternParameterException {
        BookShelf bookShelf = new BookShelf();
        // -----
        // ---C-
        // ---CT
        // --CCC
        // C-TCC
        // TTTTT
        List<Tile> one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.CAT));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        String name = "6+";
        Pattern pattern1a = new Adjacent(name,6, 8);
        assertEquals(16, pattern1a.getPatternFunction().apply(myBookshelf));

        String name2 = "5";
        Pattern pattern2a = new Adjacent(name2,5, 1, 5);
        assertEquals(0, pattern2a.getPatternFunction().apply(myBookshelf));

        String name3 = "4";
        Pattern pattern3a = new Adjacent(name3,4, 1, 3);
        assertEquals(0, pattern3a.getPatternFunction().apply(myBookshelf));

        String name4 = "3";
        Pattern pattern4a = new Adjacent(name3,3, 1, 2);
        assertEquals(0, pattern4a.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void test5() throws InvalidPatternParameterException {
        BookShelf bookShelf = new BookShelf();
        // -----
        // ---CC
        // ---CT
        // ---CC
        // C-TFF
        // TTTTC
        List<Tile> one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.CAT));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        String name = "6+";
        Pattern pattern1a = new Adjacent(name,6, 8);
        assertEquals(0, pattern1a.getPatternFunction().apply(myBookshelf));

        String name2 = "5";
        Pattern pattern2a = new Adjacent(name2,5, 1, 5);
        assertEquals(10, pattern2a.getPatternFunction().apply(myBookshelf));

        String name3 = "4";
        Pattern pattern3a = new Adjacent(name3,4, 1, 3);
        assertEquals(0, pattern3a.getPatternFunction().apply(myBookshelf));

        String name4 = "3";
        Pattern pattern4a = new Adjacent(name3,3, 1, 2);
        assertEquals(0, pattern4a.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void test4() throws InvalidPatternParameterException {
        BookShelf bookShelf = new BookShelf();
        // -----
        // ---CT
        // ---CT
        // ---CC
        // C-TFF
        // TTTCC
        List<Tile> one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        String name = "6+";
        Pattern pattern1a = new Adjacent(name,6, 8);
        assertEquals(0, pattern1a.getPatternFunction().apply(myBookshelf));

        String name2 = "5";
        Pattern pattern2a = new Adjacent(name2,5, 1, 5);
        assertEquals(0, pattern2a.getPatternFunction().apply(myBookshelf));

        String name3 = "4";
        Pattern pattern3a = new Adjacent(name3,4, 1, 3);
        assertEquals(6, pattern3a.getPatternFunction().apply(myBookshelf));

        String name4 = "3";
        Pattern pattern4a = new Adjacent(name3,3, 1, 2);
        assertEquals(0, pattern4a.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void test3() throws InvalidPatternParameterException {
        BookShelf bookShelf = new BookShelf();
        // -----
        // ----T
        // ---CT
        // ---CC
        // C-TFF
        // CTTCC
        List<Tile> one =
                List.of(new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        String name = "6+";
        Pattern pattern1a = new Adjacent(name,6, 8);
        assertEquals(0, pattern1a.getPatternFunction().apply(myBookshelf));

        String name2 = "5";
        Pattern pattern2a = new Adjacent(name2,5, 1, 5);
        assertEquals(0, pattern2a.getPatternFunction().apply(myBookshelf));

        String name3 = "4";
        Pattern pattern3a = new Adjacent(name3,4, 1, 3);
        assertEquals(0, pattern3a.getPatternFunction().apply(myBookshelf));

        String name4 = "3";
        Pattern pattern4a = new Adjacent(name3,3, 1, 2);
        assertEquals(4, pattern4a.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void test2GroupsOf4() throws InvalidPatternParameterException {
        BookShelf bookShelf = new BookShelf();
        // -----
        // ---CT
        // ---CT
        // ---CC
        // C-TFF
        // TTTCC
        List<Tile> one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        String name = "4";
        Pattern pattern = new Adjacent(name,4, 3, 1);
        assertEquals(0, pattern.getPatternFunction().apply(myBookshelf));
        // groups are deleted anyway
        pattern = new Adjacent(name,4, 2, 1);
        assertEquals(0, pattern.getPatternFunction().apply(myBookshelf));
        myBookshelf = bookShelf.getState();
        assertEquals(2, pattern.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void test6GroupsOf2() throws InvalidPatternParameterException {
        BookShelf bookShelf = new BookShelf();
        // -----
        // ---CT
        // ---CT
        // -BBCC
        // CBTFF
        // TTFCC
        List<Tile> one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.FRAME), new Tile(TileType.TROPHY), new Tile(TileType.BOOK));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.CAT), new Tile(TileType.FRAME), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        String n = "2";
        Pattern pattern = new Adjacent(n,2, 7, 1);
        assertEquals(0, pattern.getPatternFunction().apply(myBookshelf));
        //groups are deleted anyway
        pattern = new Adjacent(n,2, 1, 1);
        assertEquals(0, pattern.getPatternFunction().apply(myBookshelf));
        myBookshelf = bookShelf.getState();
        pattern = new Adjacent(n,2, 6, 1);
        assertEquals(6, pattern.getPatternFunction().apply(myBookshelf));
        pattern = new Adjacent(n,2, 1, 1);
        assertEquals(0, pattern.getPatternFunction().apply(myBookshelf));
    }
}