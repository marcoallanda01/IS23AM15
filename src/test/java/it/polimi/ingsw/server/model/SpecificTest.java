package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidPatternParameterException;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.managers.patterns.SpecificPattern;
import it.polimi.ingsw.utils.ObjectCleaner;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class SpecificTest {
    //bookshelf.get(5).get(4) returns the top right corner
    //bookshelf.get(n) returns the nth row starting from the bottom
    //bookshelf.get(n).get(m) returns the mth element (starting from left) of the nth column (starting from right)
    @Test
    void testGetPatternFunction() throws InvalidPatternParameterException {
        // some random tests
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
        Pattern pattern1a = new SpecificPattern(name, masks, 4, false, 1, 1);
        assertEquals(1, pattern1a.getPatternFunction().apply(myBookshelf));
        Pattern pattern1b = new SpecificPattern(name, masks, 5, false, 1, 1);
        assertEquals(0, pattern1b.getPatternFunction().apply(myBookshelf));

        String name2 = "6squaresWith2Types";
        Pattern pattern2a = new SpecificPattern(name2, masks, 6, false, 1, 2);
        assertEquals(1, pattern2a.getPatternFunction().apply(myBookshelf));
        Pattern pattern2b = new SpecificPattern(name2, masks, 7, false, 1, 2);
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
        Pattern pattern3a = new SpecificPattern(name3, Xmasks, 2, false, 1, 1);
        assertEquals(1, pattern3a.getPatternFunction().apply(myBookshelf));
        Pattern pattern3b = new SpecificPattern(name3, Xmasks, 4, false, 1, 1);
        assertEquals(0, pattern3b.getPatternFunction().apply(myBookshelf));
        String name4 = "4With2Types";
        Pattern pattern4a = new SpecificPattern(name4, Xmasks, 4, false, 1, 2);
        assertEquals(1, pattern4a.getPatternFunction().apply(myBookshelf));
        Pattern pattern4b = new SpecificPattern(name4, Xmasks, 5, false, 1, 2);
        assertEquals(0, pattern4b.getPatternFunction().apply(myBookshelf));

        List<List<List<Boolean>>> Cornermasks = new ArrayList<>();
        Cornermasks.add(new ArrayList<>());
        for (int i = 0; i < 6; i++) {
            Cornermasks.get(0).add(new ArrayList<>());
            for (int j = 0; j < 5; j++) {
                Cornermasks.get(0).get(i).add(false);
            }
        }
        Cornermasks.get(0).get(0).set(0, true);
        Cornermasks.get(0).get(0).set(4, true);
        Cornermasks.get(0).get(5).set(0, true);
        Cornermasks.get(0).get(5).set(4, true);

        String name5 = "Corners";
        Pattern pattern5a = new SpecificPattern(name2, Cornermasks, 1, false, 1, 1);
        assertEquals(1, pattern5a.getPatternFunction().apply(myBookshelf));
        Pattern pattern5b = new SpecificPattern(name2, Cornermasks, 2, false, 1, 1);
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
        Pattern pattern7a = new SpecificPattern(name7, Couplermasks, 14, false, 1, 1);
        assertEquals(1, pattern7a.getPatternFunction().apply(myBookshelf));
        Pattern pattern7b = new SpecificPattern(name7, Couplermasks, 15, false, 1, 1);
        assertEquals(0, pattern7b.getPatternFunction().apply(myBookshelf));
        // you can divide the bookshelf in 15 groups of 2 (any color)

        Pattern pattern7c = new SpecificPattern(name7, Couplermasks, 15, false, 1, 6);
        assertEquals(1, pattern7c.getPatternFunction().apply(myBookshelf));
        Pattern pattern7d = new SpecificPattern(name7, Couplermasks, 16, false, 1, 6);
        assertEquals(0, pattern7d.getPatternFunction().apply(myBookshelf));


        List<List<List<Boolean>>> lmasks = new ArrayList<>();
        lmasks.add(new ArrayList<>());
        for (int i = 0; i < 1; i++) {
            lmasks.get(0).add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                lmasks.get(0).get(i).add(false);
            }
        }
        lmasks.get(0).get(0).set(0, true);
        lmasks.get(0).get(0).set(1, true);
        lmasks.get(0).get(0).set(2, true);
        lmasks.add(new ArrayList<>());
        for (int i = 0; i < 3; i++) {
            lmasks.get(1).add(new ArrayList<>());
            for (int j = 0; j < 1; j++) {
                lmasks.get(1).get(i).add(false);
            }
        }
        lmasks.get(1).get(0).set(0, true);
        lmasks.get(1).get(1).set(0, true);
        lmasks.get(1).get(2).set(0, true);


        String name8 = "LinesOf3";
        Pattern pattern8a = new SpecificPattern(name8, lmasks, 6, false, 1, 1);
        assertEquals(1, pattern8a.getPatternFunction().apply(myBookshelf));
        Pattern pattern8b = new SpecificPattern(name8, lmasks, 7, false, 1, 1);
        assertEquals(0, pattern8b.getPatternFunction().apply(myBookshelf));
        Pattern pattern8c = new SpecificPattern(name8, lmasks, 10, false, 1, 6);
        assertEquals(1, pattern8c.getPatternFunction().apply(myBookshelf));
        Pattern pattern8d = new SpecificPattern(name8, lmasks, 11, false, 1, 6);
        assertEquals(0, pattern8d.getPatternFunction().apply(myBookshelf));

    }

    // NB: this test represents the 6 couples card if done with specific
    // for coherency we don't use specific for the 6 couples card (we use adjacent)
    @Test
    void test6Couples() throws InvalidPatternParameterException {
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

        String name = "Couples";
        Pattern achievablePattern = new SpecificPattern(name, Couplermasks, 6, false, 1, 1);
        Pattern unachievablePattern = new SpecificPattern(name, Couplermasks, 7, false, 1, 1);

        BookShelf bookShelf = new BookShelf();
        // -C---
        // -C---
        // -CC--
        // CBCC-
        // CCTC-
        // TTBTT
        List<Tile> one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.BOOK), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.TROPHY));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        assertEquals(1, achievablePattern.getPatternFunction().apply(myBookshelf));
        assertEquals(0, unachievablePattern.getPatternFunction().apply(myBookshelf));
    }

    @Test
    void testDiagonal() throws InvalidPatternParameterException {
        // diagonal mask
        List<List<List<Boolean>>> masks = new ArrayList<>();
        for (int m = 0; m < 2; m++) {
            masks.add(new ArrayList<>());
            for (int i = 0; i < 5; i++) {
                masks.get(m).add(new ArrayList<>());
                for (int j = 0; j < 5; j++) {
                    masks.get(m).get(i).add(false);
                }
            }
        }
        // first diagonal
        masks.get(0).get(0).set(0, true);
        masks.get(0).get(1).set(1, true);
        masks.get(0).get(2).set(2, true);
        masks.get(0).get(3).set(3, true);
        masks.get(0).get(4).set(4, true);
        // second diagonal
        masks.get(1).get(4).set(0, true);
        masks.get(1).get(3).set(1, true);
        masks.get(1).get(2).set(2, true);
        masks.get(1).get(1).set(3, true);
        masks.get(1).get(0).set(4, true);
        // defining the pattern
        String name = "Diagonal";
        Pattern achievablePattern = new SpecificPattern(name, masks, 1, false, 1, 1);
        Pattern unachievablePattern = new SpecificPattern(name, masks, 2, false, 1, 1);

        // first test
        BookShelf bookShelf = new BookShelf();
        // -CC-G
        // -CBGP
        // -CGPP
        // CGPCG
        // CPTCG
        // PTBTT
        List<Tile> one =
                List.of(new Tile(TileType.PLANT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(one, 0);
        List<Tile> two = List.of(new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.BOOK), new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.GAME));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.PLANT), new Tile(TileType.PLANT), new Tile(TileType.GAME));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        assertEquals(1, achievablePattern.getPatternFunction().apply(myBookshelf));
        assertEquals(0, unachievablePattern.getPatternFunction().apply(myBookshelf));

        // second test (reversed columns)
        bookShelf = new BookShelf();
        // -CC-G
        // -CBGP
        // -CGPP
        // CGPCG
        // CPTCG
        // PTBTT
        five =
                List.of(new Tile(TileType.PLANT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(five, 4);
        four = List.of(new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT));
        bookShelf.insertTiles(four, 3);
        three =
                List.of(new Tile(TileType.BOOK), new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(three, 2);
        two =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.GAME));
        bookShelf.insertTiles(two, 1);
        one =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.PLANT), new Tile(TileType.PLANT), new Tile(TileType.GAME));
        bookShelf.insertTiles(one, 0);

        myBookshelf = bookShelf.getState();

        assertEquals(1, achievablePattern.getPatternFunction().apply(myBookshelf));
        assertEquals(0, unachievablePattern.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void test4Rows3Color() throws InvalidPatternParameterException {
        // row mask
        List<List<List<Boolean>>> masks = new ArrayList<>();
        for (int m = 0; m < 1; m++) {
            masks.add(new ArrayList<>());
            for (int i = 0; i < 1; i++) {
                masks.get(m).add(new ArrayList<>());
                for (int j = 0; j < 5; j++) {
                    masks.get(m).get(i).add(false);
                }
            }
        }
        // row
        masks.get(0).get(0).set(0, true);
        masks.get(0).get(0).set(1, true);
        masks.get(0).get(0).set(2, true);
        masks.get(0).get(0).set(3, true);
        masks.get(0).get(0).set(4, true);;
        // defining the pattern
        String name = "4Rows3Color";
        Pattern achievablePattern = new SpecificPattern(name, masks, 4, false, 1, 3);
        Pattern unachievablePattern = new SpecificPattern(name, masks, 5, false, 1, 3);

        // first test
        BookShelf bookShelf = new BookShelf();
        // -CC-G
        // BBBBB ok
        // CCGPP ok
        // CGPCG ok
        // CPTCG not ok
        // PTBTT ok
        List<Tile> one =
                List.of(new Tile(TileType.PLANT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.BOOK));
        bookShelf.insertTiles(one, 0);
        List<Tile> two =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(two, 1);
        List<Tile> three =
                List.of(new Tile(TileType.BOOK), new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(three, 2);
        List<Tile> four =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.BOOK));
        bookShelf.insertTiles(four, 3);
        List<Tile> five =
                List.of(new Tile(TileType.TROPHY), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(five, 4);

        List<List<Optional<Tile>>> myBookshelf = bookShelf.getState();

        assertEquals(1, achievablePattern.getPatternFunction().apply(myBookshelf));
        assertEquals(0, unachievablePattern.getPatternFunction().apply(myBookshelf));
    }
    @Test
    void testCorners() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row1 = List.of(true, false, false, false, true);
        List<Boolean> row2 = List.of(false, false, false, false, false);
        List<Boolean> row3 = List.of(false, false, false, false, false);
        List<Boolean> row4 = List.of(false, false, false, false, false);
        List<Boolean> row5 = List.of(false, false, false, false, false);
        List<Boolean> row6 = List.of(true, false, false, false, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        mask.add(row6);
        masks.add(mask);
        Pattern achievablePattern = new SpecificPattern("CORNERS", masks, 1, false, 1, 1);
        Pattern unAchievablePattern = new SpecificPattern("CORNERS", masks, 1, false, 2, 2);


        // | P | B | B | B | P |
        // | B | B | B | B | B |
        // | C | B | G | B | C |
        // | C | P | T | P | C |
        // | C | B | G | B | C |
        // | P | B | B | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | P | B | B | B | C |
        // | B | B | B | B | B |
        // | C | B | G | B | C |
        // | C | P | T | P | C |
        // | C | B | G | B | C |
        // | P | B | B | B | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.CAT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(1, unAchievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void test2ColumnsColorful() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row6 = List.of(true);
        List<Boolean> row5 = List.of(true);
        List<Boolean> row4 = List.of(true);
        List<Boolean> row3 = List.of(true);
        List<Boolean> row2 = List.of(true);
        List<Boolean> row1 = List.of(true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        mask.add(row6);
        masks.add(mask);
        Pattern easilyAchievablePattern = new SpecificPattern("1_COLUMNS_COLOURFUL", masks, 1, false, 6, 6);
        Pattern achievablePattern = new SpecificPattern("2_COLUMNS_COLOURFUL", masks, 2, false, 6, 6);
        Pattern unAchievablePattern = new SpecificPattern("3_COLUMNS_COLOURFUL", masks, 3, false, 6, 6);

        // | F | - | G | C | P |
        // | C | - | B | F | B |
        // | T | B | F | T | B |
        // | G | G | T | G | B |
        // | B | B | P | B | B |
        // | P | C | C | C | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | F | - | B | C | P |
        // | C | - | B | F | B |
        // | T | B | F | T | B |
        // | G | G | T | G | B |
        // | B | B | P | B | B |
        // | P | C | C | C | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(1, easilyAchievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void test2Squares() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row2 = List.of(true, true);
        List<Boolean> row1 = List.of(true, true);
        mask.add(row1);
        mask.add(row2);
        masks.add(mask);
        Pattern easilyAchievablePattern = new SpecificPattern("1_SQUARE", masks, 1, false, 1, 1);
        Pattern achievablePattern = new SpecificPattern("2_SQUARES", masks, 2, false, 1, 1);
        Pattern unAchievablePattern = new SpecificPattern("3_SQUARES", masks, 3, false, 1, 1);

        // | F | - | G | C | P |
        // | C | - | B | F | B |
        // | T | B | F | T | B |
        // | G | G | T | B | B |
        // | G | G | P | B | B |
        // | P | C | C | C | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | F | - | B | C | P |
        // | C | - | B | F | B |
        // | T | B | F | T | B |
        // | G | G | T | B | B |
        // | B | B | P | B | B |
        // | P | C | C | C | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(1, easilyAchievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void test2RowsColorful() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row1 = List.of(true, true, true, true, true);
        mask.add(row1);
        masks.add(mask);
        Pattern easilyAchievablePattern = new SpecificPattern("1_ROW", masks, 1, false, 5, 5);
        Pattern achievablePattern = new SpecificPattern("2_ROWS", masks, 2, false, 5, 5);
        Pattern unAchievablePattern = new SpecificPattern("3_ROWS", masks, 3, false, 5, 5);

        // | F | - | G | C | P |
        // | C | - | B | F | B |
        // | T | B | F | P | G |
        // | G | G | T | B | B |
        // | G | G | P | B | B |
        // | P | C | T | G | F |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.TROPHY), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | F | - | G | C | P |
        // | C | - | B | F | B |
        // | T | B | F | P | G |
        // | G | G | T | B | B |
        // | G | G | P | B | B |
        // | P | C | C | C | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.CAT), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.GAME), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(1, easilyAchievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void test3Columns3Color() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row6 = List.of(true);
        List<Boolean> row5 = List.of(true);
        List<Boolean> row4 = List.of(true);
        List<Boolean> row3 = List.of(true);
        List<Boolean> row2 = List.of(true);
        List<Boolean> row1 = List.of(true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        mask.add(row6);
        masks.add(mask);
        Pattern easilyAchievablePattern = new SpecificPattern("2_COLUMNS_3_COLOR", masks, 2, false, 1, 3);
        Pattern achievablePattern = new SpecificPattern("3_COLUMNS_3_COLOR", masks, 3, false, 1, 3);
        Pattern unAchievablePattern = new SpecificPattern("4_COLUMNS_3_COLOR", masks, 4, false, 1, 3);

        // | F | - | G | C | P |
        // | F | - | B | F | B |
        // | F | B | F | T | B |
        // | F | G | T | T | B |
        // | B | B | P | C | B |
        // | P | C | C | C | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | F | - | B | C | P |
        // | F | - | B | F | B |
        // | F | B | F | T | B |
        // | F | G | T | G | B |
        // | B | B | P | B | B |
        // | P | C | C | C | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(1, easilyAchievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void testX() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row3 = List.of(true,false,true);
        List<Boolean> row2 = List.of(false,true,false);
        List<Boolean> row1 = List.of(true, false, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        masks.add(mask);

        Pattern easilyAchievablePattern = new SpecificPattern("MULTICOLOR_X", masks, 1, false, 1, 2);
        Pattern achievablePattern = new SpecificPattern("X", masks, 1, false, 1, 1);
        Pattern unAchievablePattern = new SpecificPattern("2_X", masks, 2, false, 1, 1);

        // | F | - | G | C | P |
        // | B | - | B | F | B |
        // | F | B | F | T | B |
        // | B | G | B | T | B |
        // | B | B | P | C | B |
        // | P | C | C | C | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | F | - | B | C | P |
        // | F | - | B | F | B |
        // | F | B | F | T | B |
        // | F | G | B | G | B |
        // | B | B | P | B | B |
        // | P | C | C | C | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(1, easilyAchievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void test8Same() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row1 = List.of(true);
        mask.add(row1);
        masks.add(mask);

        Pattern easilyAchievablePattern = new SpecificPattern("7", masks, 7, true, 1, 1);
        Pattern achievablePattern = new SpecificPattern("8", masks, 8, true, 1, 1);
        Pattern unAchievablePattern = new SpecificPattern("9", masks, 9, true, 1, 1);

        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of();
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.BOOK));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | - | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of();
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.BOOK));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(1, easilyAchievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void testStairs() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row5 = List.of(true, false, false, false, false);
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        masks.add(mask);
        Pattern achievablePattern = new SpecificPattern("CORNERS", masks, 1, false, 1, 6, true);
        Pattern unAchievablePattern = new SpecificPattern("CORNERS", masks, 2, false, 1, 6, true);


        // | - | - | - | - | - |
        // | B | - | - | - | - |
        // | B | B | - | - | - |
        // | B | B | T | - | - |
        // | B | B | P | B | - |
        // | P | C | C | C | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();
        assertEquals(1, achievablePattern.getPatternFunction().apply(bookshelfState));
        assertEquals(0, unAchievablePattern.getPatternFunction().apply(bookshelfState));

        // | P | - | - | - | - |
        // | B | B | - | - | - |
        // | B | B | - | - | - |
        // | B | P | T | - | - |
        // | B | B | G | B | C |
        // | P | B | B | B | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.PLANT));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(0, achievablePattern.getPatternFunction().apply(bookshelfState));
    }
    @Test
    void test3Columns3ColorWithDeletion() throws InvalidPatternParameterException {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row6 = List.of(true);
        List<Boolean> row5 = List.of(true);
        List<Boolean> row4 = List.of(true);
        List<Boolean> row3 = List.of(true);
        List<Boolean> row2 = List.of(true);
        List<Boolean> row1 = List.of(true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        mask.add(row6);
        masks.add(mask);
        Pattern easilyAchievablePattern = new SpecificPattern("2_COLUMNS_3_COLOR", masks, 2, false, 1, 3);
        Pattern achievablePattern = new SpecificPattern("3_COLUMNS_3_COLOR", masks, 3, false, 1, 3);
        Pattern unAchievablePattern = new SpecificPattern("4_COLUMNS_3_COLOR", masks, 4, false, 1, 3);
        Function<List<List<Optional<Tile>>>, Integer> egpf = easilyAchievablePattern.getPatternFunction();
        Function<List<List<Optional<Tile>>>, Integer> agpf = achievablePattern.getPatternFunction();
        Function<List<List<Optional<Tile>>>, Integer> ugpf = unAchievablePattern.getPatternFunction();
        new ObjectCleaner(easilyAchievablePattern);
        easilyAchievablePattern = null;
        achievablePattern = null;
        unAchievablePattern = null;
        System.gc();
        // | F | - | G | C | P |
        // | F | - | B | F | B |
        // | F | B | F | T | B |
        // | F | G | T | T | B |
        // | B | B | P | C | B |
        // | P | C | C | C | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        List<Tile> secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        List<Tile> thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.GAME));
        bookShelf.insertTiles(thirdRow, 2);
        List<Tile> fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.CAT), new Tile(TileType.TROPHY), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        List<Tile> fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        List<List<Optional<Tile>>> bookshelfState = bookShelf.getState();



        assertEquals(1, agpf.apply(bookshelfState));
        assertEquals(0, ugpf.apply(bookshelfState));

        // | F | - | B | C | P |
        // | F | - | B | F | B |
        // | F | B | F | T | B |
        // | F | G | T | G | B |
        // | B | B | P | B | B |
        // | P | C | C | C | P |
        bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        firstRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME), new Tile(TileType.FRAME));
        bookShelf.insertTiles(firstRow, 0);
        secondRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.BOOK));
        bookShelf.insertTiles(secondRow, 1);
        thirdRow = List.of(new Tile(TileType.CAT), new Tile(TileType.PLANT), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.BOOK), new Tile(TileType.BOOK));
        bookShelf.insertTiles(thirdRow, 2);
        fourthRow = List.of(new Tile(TileType.CAT), new Tile(TileType.BOOK), new Tile(TileType.GAME), new Tile(TileType.TROPHY), new Tile(TileType.FRAME), new Tile(TileType.CAT));
        bookShelf.insertTiles(fourthRow, 3);
        fifthRow = List.of(new Tile(TileType.PLANT), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.PLANT));
        bookShelf.insertTiles(fifthRow, 4);
        bookshelfState = bookShelf.getState();
        assertEquals(1, egpf.apply(bookshelfState));
        assertEquals(0, agpf.apply(bookshelfState));
    }
    @Test
    void testNullMasks() {
        try {
            Pattern pattern = new SpecificPattern("", null, 1, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("masks cannot be null", e.getMessage());
        }
    }
    @Test
    void testNullInMasks() {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row5 = List.of(true, false, false, false, false);
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        masks.add(mask);
        masks.add(null);
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("masks cannot contain a null element", e.getMessage());
        }
    }
    @Test
    void testNullRowInMask() {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row5 = List.of(true, false, false, false, false);
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        mask.add(row1);
        mask.add(null);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        masks.add(mask);
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("masks cannot contain an array containing a null element", e.getMessage());
        }
    }
    @Test
    void testNullElementInMask() {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row5 = new ArrayList<>(List.of(true, false, false, false));
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        row5.add(null);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        masks.add(mask);
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("masks cannot contain a matrix containing a null element", e.getMessage());
        }
    }
    @Test
    void testIrregularlySizedListInMask() {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row5 = List.of(true, false, false, false, false);
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        masks.add(mask);
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("masks cannot contain arrays of different lengths", e.getMessage());
        }
    }
    @Test
    void testWrongGroupNum() {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row5 = List.of(true, false, false, false, false);
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        masks.add(mask);
        try {
            Pattern pattern = new SpecificPattern("", masks, -1, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("groupNum must be strictly positive", e.getMessage());
        }
        try {
            Pattern pattern = new SpecificPattern("", masks, 0, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("groupNum must be strictly positive", e.getMessage());
        }
        try {
            Pattern pattern = new SpecificPattern("", masks, 17, false, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("groupNum must be less than or equal to 16", e.getMessage());
        }
    }
    @Test
    void testColorCoherency() {
        // Creating the pattern
        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<List<Boolean>> mask = new ArrayList<>();
        List<Boolean> row5 = List.of(true, false, false, false, false);
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        mask.add(row1);
        mask.add(row2);
        mask.add(row3);
        mask.add(row4);
        mask.add(row5);
        masks.add(mask);
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, false, 0, 1, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("minColor must be strictly positive", e.getMessage());
        }
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, false, 1, 0, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("maxColor must be strictly positive", e.getMessage());
        }
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, false, 2, 1, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("minC must be less than or equal to maxColor", e.getMessage());
        }
        try {
            Pattern pattern = new SpecificPattern("", masks, 1, true, 1, 6, true);
        } catch (InvalidPatternParameterException e) {
            assertEquals("if sgc is set to true, both minColor and maxColor must be 1", e.getMessage());
        }
    }

    @Test
    void getTransposedMasks() throws InvalidPatternParameterException {

        List<List<List<Boolean>>> masks = new ArrayList<>();
        List<Boolean> row5 = List.of(true, false, false, false, false);
        List<Boolean> row4 = List.of(true, true, false, false, false);
        List<Boolean> row3 = List.of(true, true, true, false, false);
        List<Boolean> row2 = List.of(true, true, true, true, false);
        List<Boolean> row1 = List.of(true, true, true, true, true);
        masks.add(List.of(row1, row2, row3, row4, row5));
        row5 = List.of(true);
        row4 = List.of(true);
        row3 = List.of(true);
        row2 = List.of(true);
        row1 = List.of(true);
        masks.add(List.of(row1, row2, row3, row4, row5));

        List<List<List<Boolean>>> results = new ArrayList<>();
        List<Boolean> col5 = List.of(true, false, false, false, false);
        List<Boolean> col4 = List.of(true, true, false, false, false);
        List<Boolean> col3 = List.of(true, true, true, false, false);
        List<Boolean> col2 = List.of(true, true, true, true, false);
        List<Boolean> col1 = List.of(true, true, true, true, true);
        results.add(List.of(col1, col2, col3, col4, col5));
        col5 = List.of(true, true, true, true, true);
        results.add(List.of(col5));

        SpecificPattern pattern = new SpecificPattern("test", masks, 1, false, 1, 1);
        assertEquals(results, pattern.getTransposedMasks(masks));
    }


}