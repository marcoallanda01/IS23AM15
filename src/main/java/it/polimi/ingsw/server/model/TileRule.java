package it.polimi.ingsw.server.model;

/**
 * Enum that represents the rules of the tiles
 */
public enum TileRule {
    /**
     * No tiles can be placed here
     */
    BLOCK,
    /**
     * Tiles can be placed only if at least a two players game
     */
    TWO,
    /**
     * Tiles can be placed only if at least a three players game
     */
    THREE,
    /**
     * Tiles can be placed only if at least a four players game
     */
    FOUR
}
