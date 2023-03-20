package it.polimi.ingsw.modules;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookShelfTest {

    @Test
    void insertTiles() {
        BookShelf bookShelf = new BookShelf();
        assertTrue(bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 2));
        assertTrue(bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.PLANT)), 2));
        assertFalse(bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.BOOK)), 2));
        assertNull(bookShelf.getTile(2, 5));
        assertFalse(bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.BOOK)), 6));
        //System.out.println(bookShelf);
    }

    @Test
    void getTile() {
        BookShelf bookShelf = new BookShelf();
        bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 2);
        assertEquals(TileType.BOOK, bookShelf.getTile(2, 0).getType());
    }

    @Test
    void clearBookShelf() {
        BookShelf bookShelf = new BookShelf();
        bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 2);
        bookShelf.clearBookShelf();
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 4; j++) {
                assertNull(bookShelf.getTile(i, j));
            }
        }
    }

    @Test
    void getMaxColumnSpace() {
        BookShelf bookShelf = new BookShelf();
        assertEquals(6, bookShelf.getMaxColumnSpace());
        bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 2);
        assertEquals(6, bookShelf.getMaxColumnSpace());
        bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 0);
        bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 1);
        bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 3);
        bookShelf.insertTiles(List.of(new Tile(-1, -1, TileType.BOOK), new Tile(-1, -1, TileType.CAT), new Tile(-1, -1, TileType.TROPHY)), 4);
        assertEquals(3, bookShelf.getMaxColumnSpace());
    }
}