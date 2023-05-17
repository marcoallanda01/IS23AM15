package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIRenderer;
import it.polimi.ingsw.server.model.BookShelf;
import it.polimi.ingsw.server.model.LivingRoomBoard;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

class CLIRendererTest {

    @Test
    void printLivingRoomBoard() {
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(4);
        livingRoomBoard.fillBoard();
        Set<Tile> tiles = new HashSet<>(livingRoomBoard.getAllTiles());
        CLIRenderer.printLivingRoomBoard(tiles);
    }

    @Test
    void printLivingRoomBoardLine() {
        LivingRoomBoard livingRoomBoard = new LivingRoomBoard(4);
        livingRoomBoard.fillBoard();
        Set<Tile> tiles = new HashSet<>(livingRoomBoard.getAllTiles());
        CLIRenderer.printLivingRoomBoardLine(tiles, 0);
    }

    @Test
    void printBookshelves() {
        BookShelf bookShelf1 = new BookShelf();
        fillBookshelf(bookShelf1, 0);
        BookShelf bookShelf2 = new BookShelf();
        fillBookshelf(bookShelf2, 1);
        BookShelf bookShelf3 = new BookShelf();
        fillBookshelf(bookShelf3, 2);
        Set<Tile> tiles1 = bookShelf1.getState().stream().flatMap(List::stream).map(optional -> optional.orElse(null)).collect(Collectors.toSet());
        Set<Tile> tiles2 = bookShelf2.getState().stream().flatMap(List::stream).map(optional -> optional.orElse(null)).collect(Collectors.toSet());
        Set<Tile> tiles3 = bookShelf3.getState().stream().flatMap(List::stream).map(optional -> optional.orElse(null)).collect(Collectors.toSet());
        Map<String, Set<Tile>> bookShelf = Map.of("Marco", tiles1, "Luca", tiles2, "Pietro1234", tiles3);
        CLIRenderer.printBookshelves(bookShelf, "Luca", "Marco");
    }

    @Test
    void printBookshelfLine() {
        BookShelf bookShelf = new BookShelf();
        fillBookshelf(bookShelf, 0);
        Set<Tile> tiles = bookShelf.getState().stream().flatMap(List::stream).map(optional -> optional.orElse(null)).collect(Collectors.toSet());
        CLIRenderer.printBookshelfLine(tiles, 0);
    }

    void fillBookshelf(BookShelf bookShelf, int seed) {
        List<Tile> firstColumn = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            firstColumn.add(new Tile(0, 0, TileType.BOOK));
        }
        List<Tile> secondColumn = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            secondColumn.add(new Tile(0, 0, TileType.CAT));
        }
        List<Tile> thirdColumn = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            thirdColumn.add(new Tile(0, 0, TileType.TROPHY));
        }
        List<Tile> fourthColumn = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            fourthColumn.add(new Tile(0, 0, TileType.GAME));
        }
        List<Tile> fifthColumn = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            fifthColumn.add(new Tile(0, 0, TileType.PLANT));
        }
        List<List<Tile>> columns = new ArrayList<>();
        columns.add(firstColumn);
        columns.add(secondColumn);
        columns.add(thirdColumn);
        columns.add(fourthColumn);
        columns.add(fifthColumn);

        Collections.shuffle(columns, new Random(seed));
        for (int i = 0; i < 5; i++) {
            if (i != 3)
                bookShelf.insertTiles(columns.get(i), i);
        }
    }

    @Test
    void printChat() {
    }

    @Test
    void printEndGame() {
        Map<String, Integer> points = Map.of("Marco", 20, "Luca", 15, "Pietro1234", 30);
        CLIRenderer.printEndGame(points, "Pietro1234");
    }
}