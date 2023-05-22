package it.polimi.ingsw.server.model.managers.patterns;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.exceptions.InvalidPatternParameterException;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;

import java.util.*;
import java.util.function.Function;

public class AdjacentPattern extends Pattern {
    private final Integer points;
    private final Integer minGroups;
    private final Integer minTiles;
    /**
     * @param name the name of the pattern
     * @param minTiles the minimum amount of tiles a group should have for it to be deleted
     * @param points the points to be given for each deleted group
     */
    public AdjacentPattern(String name, int minTiles, int points) throws InvalidPatternParameterException {
        super(name);
        this.points = points;
        this.minTiles = minTiles;
        this.minGroups = 1;
        this.checkParameters();
    }
    /**
     * @param name the name of the pattern
     * @param minTiles the minimum amount of tiles a group should have for it to be deleted
     * @param minGroups the minimum number of groups to be found to return the points (and not 0)
     * @param points the points to be given for each deleted group
     */
    public AdjacentPattern(String name, int minTiles, int minGroups, int points) throws InvalidPatternParameterException {
        super(name);
        this.points = points;
        this.minTiles = minTiles;
        this.minGroups = minGroups;
        this.checkParameters();
    }
    /**
     * Checks the constructor parameters
     * @throws InvalidPatternParameterException if the parameters are invalid
     */
    private void checkParameters() throws InvalidPatternParameterException {
        if (minTiles <= 0) {
            throw new InvalidPatternParameterException("minTiles must be strictly positive");
        }
        if (minGroups <= 0) {
            throw new InvalidPatternParameterException("minGroups must be strictly positive");
        }
    }

    /**
     * @return a function that given the bookshelf EDITS the bookshelf,
     * deleting all the tiles contained in the groups with at least minTiles tiles
     * if the number of deleted groups is more than or equal to minGroups it also returns the points
     * (that could be interpreted as the actual points or just used to check if it satisfies the pattern)
     */
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        final int minTiles = this.minTiles;
        final int minGroups = this.minGroups;
        final int points = this.points;
        return (bookshelf) -> {
            Integer groupsFound = 0;
            while (findAndDeleteGroup(bookshelf, minTiles)) groupsFound++;
            return groupsFound >= minGroups ? points * groupsFound : 0;
        };
    }
    /**
     * @param bookshelf the bookshelf
     * @param minTiles the minimum amount of tiles a group should have for it to be deleted
     * @return if a group of at least minTiles has been found (and deleted)
     */
    private static boolean findAndDeleteGroup(List<List<Optional<Tile>>> bookshelf, Integer minTiles) {
        List<List<Boolean>> mask = new ArrayList<>();
        for (int i = 0; i < bookshelf.size(); i++) {
            mask.add(new ArrayList<>());
            for (int j = 0; j < bookshelf.get(i).size(); j++) {
                mask.get(i).add(Boolean.FALSE);
            }
        }
        for (int i = 0; i < bookshelf.size(); i++) {
            for (int j = 0; j < bookshelf.get(i).size(); j++) {
                if(bookshelf.get(i).get(j).isPresent()) {
                    markAdjacentTiles(bookshelf, bookshelf.get(i).get(j).get(), mask);
                    if(countAdjacentTiles(mask) >= minTiles) {
                        removeTiles(bookshelf, mask);
                        return true;
                    } else {
                        for (int k = 0; k < bookshelf.size(); k++) {
                            for (int l = 0; l < bookshelf.get(k).size(); l++) {
                                mask.get(k).set(l, Boolean.FALSE);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * Removes tiles from the given bookshelf according to the matrix
     * @param bookshelf the bookshelf
     * @param mask a matrix representing the tiles to be removed
     */
    private static void removeTiles(List<List<Optional<Tile>>> bookshelf, List<List<Boolean>> mask) {
        for (int i = 0; i < mask.size(); i++) {
            for (int j = 0; j < mask.get(i).size(); j++) {
                if (mask.get(i).get(j)) {
                    bookshelf.get(i).set(j, Optional.empty());
                }
            }
        }
    }
    /**
     * Counts the amount of true in the given mask
     * @param mask a matrix representing the tiles to be counted
     * @return the amount of true in the given matrix
     */
    private static Integer countAdjacentTiles(List<List<Boolean>> mask) {
        Integer count = 0;
        for (int i = 0; i < mask.size(); i++) {
            for (int j = 0; j < mask.get(i).size(); j++) {
                if (mask.get(i).get(j)) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * Recursive method, marks the given tile on the mask
     * calls itself on nearby tiles of the same type (right, top, left, bottom), if any
     * @param bookshelf the bookshelf
     * @param checkedTile the currently checked tile
     * @param mask a matrix representing the tiles of the same type of the starting tile
     */
    private static void markAdjacentTiles(List<List<Optional<Tile>>> bookshelf, Tile checkedTile, List<List<Boolean>> mask) {
        if (mask.get(checkedTile.getX()).get(checkedTile.getY()).equals(Boolean.FALSE)) {
            mask.get(checkedTile.getX()).set(checkedTile.getY(), Boolean.TRUE);
            if (checkedTile.getX() + 1 < bookshelf.size()) {
                Optional<Tile> nextXTile = bookshelf.get(checkedTile.getX() + 1).get(checkedTile.getY());
                if (nextXTile.isPresent()) {
                    if (nextXTile.get().getType().equals(checkedTile.getType())) {
                        markAdjacentTiles(bookshelf, nextXTile.get(), mask);
                    }
                }
            }
            if (checkedTile.getY() + 1 < bookshelf.get(0).size()) {
                Optional<Tile> nextYTile = bookshelf.get(checkedTile.getX()).get(checkedTile.getY() + 1);
                if (nextYTile.isPresent()) {
                    if (nextYTile.get().getType().equals(checkedTile.getType())) {
                        markAdjacentTiles(bookshelf, nextYTile.get(), mask);
                    }
                }
            }
            if (checkedTile.getX() - 1 > 0) {
                Optional<Tile> nextXTile = bookshelf.get(checkedTile.getX() - 1).get(checkedTile.getY());
                if (nextXTile.isPresent()) {
                    if (nextXTile.get().getType().equals(checkedTile.getType())) {
                        markAdjacentTiles(bookshelf, nextXTile.get(), mask);
                    }
                }
            }
            if (checkedTile.getY() - 1 > 0) {
                Optional<Tile> nextYTile = bookshelf.get(checkedTile.getX()).get(checkedTile.getY() - 1);
                if (nextYTile.isPresent()) {
                    if (nextYTile.get().getType().equals(checkedTile.getType())) {
                        markAdjacentTiles(bookshelf, nextYTile.get(), mask);
                    }
                }
            }
        }
    }

}
