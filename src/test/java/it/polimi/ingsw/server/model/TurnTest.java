package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.turn.PutTilesState;
import it.polimi.ingsw.server.model.turn.Turn;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {

    @Test
    void changeState() {
        Turn turn = new Turn(new Player("test"), new LivingRoomBoard(2));
        assertEquals("PickTilesState", turn.getState().toString());
        turn.changeState(new PutTilesState(turn));
        assertEquals("PutTilesState", turn.getState().toString());

    }

    @Test
    void pickTiles() {
        Player player = new Player("test");
        LivingRoomBoard board = new LivingRoomBoard(2);
        Turn turn = new Turn(player, board);
        board.fillBoard();
        List<Tile> tiles = List.of(board.getBoard().get(1).get(3), board.getBoard().get(1).get(4));
        assertTrue(turn.pickTiles(tiles));
        assertEquals(tiles, turn.getPickedTiles());
        List<Tile> retryTiles = List.of(board.getBoard().get(3).get(3), board.getBoard().get(3).get(4));
        assertFalse(turn.pickTiles(retryTiles));
    }

    @Test
    void putTiles() {
        Player player = new Player("test");
        LivingRoomBoard board = new LivingRoomBoard(2);
        Turn turn = new Turn(player, board);
        board.fillBoard();
        List<Tile> tiles = List.of(board.getBoard().get(1).get(3), board.getBoard().get(1).get(4));
        assertTrue(turn.pickTiles(tiles));
        turn.changeState(new PutTilesState(turn));
        assertTrue(turn.putTiles(tiles, 0));
        assertEquals(tiles, List.of(player.getBookShelf().getTile(0,0), player.getBookShelf().getTile(0,1)));

    }

    @Test
    void checkBoardRefill() {
        Player player = new Player("test");
        LivingRoomBoard board = new LivingRoomBoard(2);
        Turn turn = new Turn(player, board);
        assertTrue(turn.checkBoardRefill());
        board.fillBoard();
        assertFalse(turn.checkBoardRefill());
    }

    @Test
    void getCurrentPlayer() {
        Turn turn = new Turn(new Player("test"), new LivingRoomBoard(2));
        assertEquals("test", turn.getCurrentPlayer().getUserName());
    }


    @Test
    void getState() {
        Turn turn = new Turn(new Player("test"), new LivingRoomBoard(2));
        assertEquals("PickTilesState", turn.getState().toString());
    }

    @Test
    void getPickedTiles() {
        Turn turn = new Turn(new Player("test"), new LivingRoomBoard(2));
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(TileType.CAT));
        tiles.add(new Tile(TileType.PLANT));
        turn.setPickedTiles(tiles);
        assertEquals(tiles, turn.getPickedTiles());
    }
}