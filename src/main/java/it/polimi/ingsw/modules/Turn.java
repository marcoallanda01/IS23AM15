package it.polimi.ingsw.modules;

import java.util.List;

public class Turn {
    private List<Tile> pickedTiles;
    private final Player currentPlayer;
    private final LivingRoomBoard board;
    private TurnState turnState;

    /**
     * Create a new turn
     * @param player the player that is playing
     * @param board the board of the game
     */
    public Turn(Player player, LivingRoomBoard board) {
        this.currentPlayer = player;
        this.board = board;
    }

    /**
     * Check if the player can pick the tiles
     * @param tiles the tiles to pick
     * @return true if the player can pick the tiles, false otherwise
     */
    private boolean checkPick(List<Tile> tiles) {
        return tiles.size() <= this.currentPlayer.getBookShelf().getMaxColumnSpace();
    }

    /**
     * Pick the tiles from the board
     * @param tiles the tiles to pick
     * @return true if the tiles are picked, false otherwise
     */
    public boolean pickTiles(List<Tile> tiles) {
        if (this.board.checkPick(tiles)) {
            this.pickedTiles = tiles;
            this.board.removeFromBoard(tiles);
            return true;
        }
        return false;
    }

    /**
     * Put the tiles in the bookshelf
     * @param tiles the tiles to put
     * @param column the column where to put the tiles
     * @return true if the tiles are put, false otherwise
     */
    public boolean putTiles(List<Tile> tiles, int column){
        if(checkPick(this.pickedTiles)){
            return this.currentPlayer.getBookShelf().insertTiles(tiles,column);
        }
        return false;
    }
    public boolean checkBoardRefill(){
        return this.board.isToFill();
    }
    public TurnState getTurnState() {
        return this.turnState;
    }
    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
    }
}
