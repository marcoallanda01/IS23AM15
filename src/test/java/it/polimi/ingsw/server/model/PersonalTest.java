package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalTest {

    @Test
    void testGetPatternFunction() throws InvalidPatternParameterException {
        // | - | - | - | - | P |
        // | - | - | - | - | B |
        // | - | - | - | - | B |
        // | - | - | - | - | B |
        // | - | - | - | - | B |
        // | - | - | - | - | P |
        List<Tile> checks =
                List.of(new Tile(4,0,TileType.PLANT), new Tile(4,1,TileType.BOOK),
                        new Tile(4,2,TileType.BOOK), new Tile(4,3,TileType.BOOK),
                        new Tile(4,4,TileType.BOOK), new Tile(4,5,TileType.PLANT));
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 2});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 6});
        list.add(new int[]{5, 9});
        list.add(new int[]{6, 12});

        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.GAME));
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

        String name = "P";
        Pattern pattern = new Personal(name, checks, list);
        assertEquals(12, pattern.getPatternFunction().apply(bookshelfState));
    }
    void test1Right() throws InvalidPatternParameterException {
        // | - | - | - | - | G |
        // | - | - | - | - | - |
        // | - | T | - | - | - |
        // | - | - | B | - | - |
        // | G | - | - | - | - |
        // | - | P | - | F | - |
        List<Tile> checks =
                List.of(new Tile(0,1,TileType.GAME), new Tile(1, 0, TileType.PLANT),
                        new Tile(1, 3, TileType.TROPHY), new Tile(2,2,TileType.BOOK),
                        new Tile(3,0,TileType.FRAME), new Tile(4,5,TileType.GAME));
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 2});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 6});
        list.add(new int[]{5, 9});
        list.add(new int[]{6, 12});

        for (int[] arr : list) {
            System.out.println(Arrays.toString(arr));
        }
        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.GAME));
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

        String name = "P";
        Pattern pattern = new Personal(name, checks, list);
        assertEquals(2, pattern.getPatternFunction().apply(bookshelfState));
    }
    void test2Right() throws InvalidPatternParameterException {
        // | - | - | - | - | G |
        // | - | - | - | - | - |
        // | - | B | - | - | - |
        // | - | - | B | - | - |
        // | G | - | - | - | - |
        // | - | P | - | F | - |
        List<Tile> checks =
                List.of(new Tile(0,1,TileType.GAME), new Tile(1, 0, TileType.PLANT),
                        new Tile(1, 3, TileType.BOOK), new Tile(2,2,TileType.BOOK),
                        new Tile(3,0,TileType.FRAME), new Tile(4,5,TileType.GAME));
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 2});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 6});
        list.add(new int[]{5, 9});
        list.add(new int[]{6, 12});

        for (int[] arr : list) {
            System.out.println(Arrays.toString(arr));
        }
        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.GAME));
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

        String name = "P";
        Pattern pattern = new Personal(name, checks, list);
        assertEquals(2, pattern.getPatternFunction().apply(bookshelfState));
    }
    void test3Right() throws InvalidPatternParameterException {
        // | - | - | - | - | G |
        // | - | - | - | F | - |
        // | - | B | - | - | - |
        // | - | - | B | - | - |
        // | G | - | - | - | - |
        // | - | P | - | - | - |
        List<Tile> checks =
                List.of(new Tile(0,1,TileType.GAME), new Tile(1, 0, TileType.PLANT),
                        new Tile(1, 3, TileType.BOOK), new Tile(2,2,TileType.BOOK),
                        new Tile(3,4,TileType.FRAME), new Tile(4,5,TileType.GAME));
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 2});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 6});
        list.add(new int[]{5, 9});
        list.add(new int[]{6, 12});

        for (int[] arr : list) {
            System.out.println(Arrays.toString(arr));
        }
        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.GAME));
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

        String name = "P";
        Pattern pattern = new Personal(name, checks, list);
        assertEquals(4, pattern.getPatternFunction().apply(bookshelfState));
    }
    void test4Right() throws InvalidPatternParameterException {
        // | - | - | - | - | P |
        // | - | - | - | F | - |
        // | - | B | - | - | - |
        // | - | - | B | - | - |
        // | G | - | - | - | - |
        // | - | P | - | - | - |
        List<Tile> checks =
                List.of(new Tile(0,1,TileType.GAME), new Tile(1, 0, TileType.PLANT),
                        new Tile(1, 3, TileType.BOOK), new Tile(2,2,TileType.BOOK),
                        new Tile(3,4,TileType.FRAME), new Tile(4,5,TileType.PLANT));
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 2});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 6});
        list.add(new int[]{5, 9});
        list.add(new int[]{6, 12});

        for (int[] arr : list) {
            System.out.println(Arrays.toString(arr));
        }
        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.GAME));
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

        String name = "P";
        Pattern pattern = new Personal(name, checks, list);
        assertEquals(6, pattern.getPatternFunction().apply(bookshelfState));
    }
    void test5Right() throws InvalidPatternParameterException {
        // | - | - | - | - | P |
        // | - | - | - | F | - |
        // | - | B | - | - | - |
        // | - | - | B | - | - |
        // | G | - | - | - | - |
        // | - | C | - | - | - |
        List<Tile> checks =
                List.of(new Tile(0,1,TileType.GAME), new Tile(1, 0, TileType.CAT),
                        new Tile(1, 3, TileType.BOOK), new Tile(2,2,TileType.BOOK),
                        new Tile(3,4,TileType.FRAME), new Tile(4,5,TileType.PLANT));
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 2});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 6});
        list.add(new int[]{5, 9});
        list.add(new int[]{6, 12});

        for (int[] arr : list) {
            System.out.println(Arrays.toString(arr));
        }
        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.GAME));
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

        String name = "P";
        Pattern pattern = new Personal(name, checks, list);
        assertEquals(9, pattern.getPatternFunction().apply(bookshelfState));
    }
    void test6Right() throws InvalidPatternParameterException {
        // | - | - | - | - | P |
        // | - | - | - | F | - |
        // | - | B | - | - | - |
        // | - | - | B | - | - |
        // | - | - | - | - | - |
        // | G | C | - | - | - |
        List<Tile> checks =
                List.of(new Tile(0,0,TileType.GAME), new Tile(1, 0, TileType.CAT),
                        new Tile(1, 3, TileType.BOOK), new Tile(2,2,TileType.BOOK),
                        new Tile(3,4,TileType.FRAME), new Tile(4,5,TileType.PLANT));
        ArrayList<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 2});
        list.add(new int[]{3, 4});
        list.add(new int[]{4, 6});
        list.add(new int[]{5, 9});
        list.add(new int[]{6, 12});

        for (int[] arr : list) {
            System.out.println(Arrays.toString(arr));
        }
        // | - | - | - | C | P |
        // | - | - | - | F | B |
        // | - | B | - | T | B |
        // | - | G | B | T | B |
        // | - | B | P | C | B |
        // | - | C | C | B | P |
        BookShelf bookShelf = new BookShelf();
        // Inserting tiles to match the pattern
        List<Tile> firstRow = List.of(new Tile(TileType.GAME));
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

        String name = "P";
        Pattern pattern = new Personal(name, checks, list);
        assertEquals(12, pattern.getPatternFunction().apply(bookshelfState));
    }
}