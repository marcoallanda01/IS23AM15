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

    static class ChatTest {
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        Player p4 = new Player("p4");
        Message m1 = new Message(p1, p2, "ciao p2");
        Message m2 = new Message(p2, p1, "ciao p1");
        Message m3 = new Message(p3, p1, "ciao p1");
        Message m4 = new Message(p1, p4, "ciao p4");
        Message m5 = new Message(p1, "ciao a tutti");
        Message m6 = new Message(p4, p1, "ciao p1");
        List<Player> players = new ArrayList<>(List.of(p1, p2, p3));
        @Test
        void getMessages() throws PlayerNotFoundException {
            Chat c = new Chat(players);
            c.addMessage(m1);
            c.addMessage(m2);
            c.addMessage(m3);
            c.addMessage(m5);
            assertThrows(PlayerNotFoundException.class, () -> c.addMessage(m4));
            assertThrows(PlayerNotFoundException.class, () -> c.addMessage(m6));
            assertEquals(c.getMessages(p1), List.of(m2, m3, m5));
            assertEquals(c.getMessages(p2), List.of(m1, m5));
        }

        @Test
        void addMessage() throws PlayerNotFoundException {
            Chat c = new Chat(players);
            assertEquals(c.getMessages(p1), new ArrayList<>());
            assertEquals(c.getMessages(p2), new ArrayList<>());
            assertEquals(c.getMessages(p3), new ArrayList<>());
            c.addMessage(m1);
            c.addMessage(m2);
            c.addMessage(m3);
            assertEquals(c.getMessages(p1), List.of(m2, m3));
            assertEquals(c.getMessages(p2), List.of(m1));
        }
    }

    static class MessageTest {

        @Test
        void getContent() {
            Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
            assertEquals(m.getContent(), m.getContent());
        }

        @Test
        void getSenderName() {
            Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
            assertEquals(m.getSenderName(), m.getSender().getUserName());
        }

        @Test
        void getDate() {
            Message m = new Message(new Player("sender"), new Player("receiver"), "ciao");
            assertEquals(m.getDate(), m.getDate().toString());
        }
    }
}