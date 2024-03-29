package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.ArrestGameException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LivingRoomBoardTest {
    private final Map<Integer, Map<Integer, TileRule>> mask = new HashMap<>();

    private void fillMask() {
        mask.put(0, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.THREE, 4, TileRule.FOUR, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(1, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.FOUR, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(2, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.THREE, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.THREE, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(3, Map.of(0, TileRule.BLOCK, 1, TileRule.FOUR, 2, TileRule.TWO, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.TWO, 7, TileRule.TWO, 8,
                TileRule.THREE));
        mask.put(4,
                Map.of(0, TileRule.FOUR, 1, TileRule.TWO, 2, TileRule.TWO, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.TWO, 7, TileRule.TWO, 8, TileRule.FOUR));
        mask.put(5,
                Map.of(0, TileRule.THREE, 1, TileRule.TWO, 2, TileRule.TWO, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.TWO, 7, TileRule.FOUR, 8, TileRule.BLOCK));
        mask.put(6, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.THREE, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.THREE, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(7, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.FOUR, 4, TileRule.TWO, 5, TileRule.TWO, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(8, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.FOUR, 5, TileRule.THREE, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
    }

    @Test
    void isToFill() {
        fillMask();
        LivingRoomBoard board = null;
        for (int i = 0; i < 100; i++) {
            board = new LivingRoomBoard(4);
            board.fillBoard();
            //System.out.println("board = " + board);
            LivingRoomBoard finalBoard = board;
            board.getBoard().forEach((row, map) -> map.forEach((col, tile) -> {
                if (mask.get(row).get(col) != TileRule.BLOCK) {
                    if (Math.random() < 0.5)
                        finalBoard.removeFromBoard(List.of(tile));
                }
            }));
            //System.out.println("board = " + board);
            //System.out.println("board.isToFill() = " + board.isToFill());
            assertFalse(board.isToFill());
        }
        System.out.println("board = " + board);
    }

    @Test
    void fillBoard() {
        fillMask();
        for (int i = 2; i < 5; i++) { // check for 2, 3, 4 players
            LivingRoomBoard board = new LivingRoomBoard(i);
            board.fillBoard();
            System.out.println("board with " + i + " players = " + board);
            int finalI = i;
            board.getBoard().forEach((row, map) -> map.forEach((col, tile) -> {
                if (mask.get(row).get(col) == TileRule.BLOCK) {
                    assertNull(tile, "tile at " + row + ", " + col + " is not null");
                } else if (mask.get(row).get(col).ordinal() + 1 <= finalI) {
                    assertNotEquals(tile, null, "tile at " + row + ", " + col + " is null");
                    assertEquals(tile.getX(), row, "tile at " + row + ", " + col + " has wrong x");
                    assertEquals(tile.getY(), col, "tile at " + row + ", " + col + " has wrong y");
                }
            }));
        }
        LivingRoomBoard board = new LivingRoomBoard(4);
        int i;
        for(i = 0; i < 4; i++) { // test for bag empty
            try{
                board.fillBoard();
                board.getBoard().values().stream().flatMap(map -> map.values().stream()).forEach(tile -> {
                    if(tile != null){
                        board.removeFromBoard(List.of(tile));
                    }
                });
            } catch (Exception e) {
                assertTrue(e instanceof ArrestGameException);
                break;
            }
        }
        assertFalse(i > 2);
    }

    @Test
    void removeFromBoard() {
        fillMask();
        for (int i = 0; i < 100; i++) {
            LivingRoomBoard board = new LivingRoomBoard(4);
            board.fillBoard();
            //System.out.println("board = " + board);
            List<Tile> tiles = new ArrayList<>();
            board.getBoard().forEach((row, map) -> map.forEach((col, tile) -> {
                if (mask.get(row).get(col) != TileRule.BLOCK) {
                    tiles.add(tile);
                }
            }));
            board.removeFromBoard(tiles);
            //System.out.println("board = " + board);
            tiles.forEach(tile -> assertNull(board.getBoard().get(tile.getX()).get(tile.getY()), "tile at " + tile.getX() + ", " + tile.getY() + " is not null"));
        }
    }

    @Test
    void checkPick() {
        fillMask();
        LivingRoomBoard board = new LivingRoomBoard(4);
        System.out.println("board = " + board);
        board.fillBoard();
        System.out.println("board = " + board);
        assertFalse(board.checkPick(List.of(board.getBoard().get(0).get(3), board.getBoard().get(1).get(3), board.getBoard().get(2).get(3))));
        assertTrue(board.checkPick(List.of(board.getBoard().get(0).get(3), board.getBoard().get(0).get(4))));
        assertFalse(board.checkPick(List.of(board.getBoard().get(0).get(3), board.getBoard().get(1).get(4))));
        assertTrue(board.checkPick(List.of(board.getBoard().get(0).get(3), board.getBoard().get(1).get(3))));
        assertTrue(board.checkPick(List.of(board.getBoard().get(5).get(0), board.getBoard().get(5).get(1))));
    }

    @Test
    void putBackInBag(){
        mask.put(0, Map.of(0, TileRule.BLOCK, 1, TileRule.TWO, 2, TileRule.TWO, 3, TileRule.TWO, 4, TileRule.TWO, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(1, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(2, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(3, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(4,
                Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8, TileRule.BLOCK));
        mask.put(5,
                Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8, TileRule.BLOCK));
        mask.put(6, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(7, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        mask.put(8, Map.of(0, TileRule.BLOCK, 1, TileRule.BLOCK, 2, TileRule.BLOCK, 3, TileRule.BLOCK, 4, TileRule.BLOCK, 5, TileRule.BLOCK, 6, TileRule.BLOCK, 7, TileRule.BLOCK, 8,
                TileRule.BLOCK));
        LivingRoomBoard board = new LivingRoomBoard(2, mask);
        assertEquals(0, (long) board.getAllTiles().size());
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.CAT));
        tiles.add(new Tile(TileType.CAT));
        tiles.add(new Tile(TileType.BOOK));
        tiles.add(new Tile(TileType.TROPHY));

        while(true){
            board.removeFromBoard(board.getAllTiles().stream().toList());
            try{
                board.fillBoard();
            }catch (ArrestGameException e){
                break;
            }
        }
        assertThrows(ArrestGameException.class, board::fillBoard);
        board.putBackInBag(tiles);
        assertEquals(0, (long) board.getAllTiles().size());
        board.fillBoard();
        assertEquals(tiles.size(), (long) board.getAllTiles().size());
        board.removeFromBoard(board.getAllTiles().stream().toList());

        assertThrows(ArrestGameException.class, board::fillBoard);
    }
}