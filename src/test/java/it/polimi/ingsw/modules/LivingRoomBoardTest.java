package it.polimi.ingsw.modules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivingRoomBoardTest {

    @Test
    void isToFill() {
    }

    @Test
    void fillBoard() {
        LivingRoomBoard board = new LivingRoomBoard(4);
        board.fillBoard();
        System.out.println("board = \n" + board);
    }

    @Test
    void removeFromBoard() {
    }

    @Test
    void checkPick() {
    }
}