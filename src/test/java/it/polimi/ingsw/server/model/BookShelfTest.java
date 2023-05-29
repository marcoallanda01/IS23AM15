package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.chat.Chat;
import it.polimi.ingsw.server.model.chat.Message;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookShelfTest {

    @Test
    void insertTiles() {
        BookShelf bookShelf = new BookShelf();
        assertNotNull(bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 2));
        assertNotNull(bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.PLANT)), 2));
        assertNull(bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.PLANT)), -2));
        assertNull(bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.PLANT)), 40));
        assertNull(bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK)), 2));
        assertNull(bookShelf.getTile(2, 5));
        assertNull(bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.BOOK), new Tile(TileType.BOOK)), 6));
        for (int i = 0; i < 5; i++) {
            assertEquals(2, bookShelf.getTile(2, i).getX());
            assertEquals(i, bookShelf.getTile(2, i).getY());
        }
        for (int i = 0; i < bookShelf.getState().size(); i++) {
            List<Optional<Tile>> column = bookShelf.getState().get(i);
            for (int j = 0; j < column.size(); j++) {
                Tile tile = column.get(j).orElse(null);
                assertEquals(tile, bookShelf.getTile(i, j));
            }
        }
        System.out.println(bookShelf);
    }

    @Test
    void getTile() {
        BookShelf bookShelf = new BookShelf();
        bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 2);
        assertEquals(TileType.BOOK, bookShelf.getTile(2, 0).getType());
    }

    @Test
    void clearBookShelf() {
        BookShelf bookShelf = new BookShelf();
        bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 2);
        bookShelf.clearBookShelf();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                assertNull(bookShelf.getTile(i, j));
            }
        }
    }

    @Test
    void getState() {
        BookShelf bookShelf = new BookShelf();
        for (int i = 0; i < 5; i++) {
            List<Tile> tiles = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                tiles.add(new Tile(TileType.getRandomTileType()));
            }
            bookShelf.insertTiles(tiles, i);
        }
        for (int i = 0; i < bookShelf.getState().size(); i++) {
            List<Optional<Tile>> column = bookShelf.getState().get(i);
            for (int j = 0; j < column.size(); j++) {
                Tile tile = column.get(j).orElse(null);
                if(tile == null)
                    continue;
                assertEquals(tile.getX(), i);
                assertEquals(tile.getY(), j);
            }
        }
    }

    @Test
    void getMaxColumnSpace() {
        BookShelf bookShelf = new BookShelf();
        assertEquals(6, bookShelf.getMaxColumnSpace());
        bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 2);
        assertEquals(6, bookShelf.getMaxColumnSpace());
        System.out.println(bookShelf);
        bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 0);
        bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 1);
        bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 3);
        bookShelf.insertTiles(List.of(new Tile(TileType.BOOK), new Tile(TileType.CAT), new Tile(TileType.TROPHY)), 4);
        assertEquals(3, bookShelf.getMaxColumnSpace());
        System.out.println(bookShelf);
    }
}