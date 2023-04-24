package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.function.Function;

public class Adjacent extends Pattern{
    private final Integer points;
    private final Integer minGroups;
    private final Integer minTiles;
    public Adjacent(String name, int minTiles, int points){
        super(name);
        this.points = points;
        this.minTiles = minTiles;
        this.minGroups = 1;
    }
    // important, signature change!!
    public Adjacent(String name, int minTiles, int minGroups, int points){
        super(name);
        this.points = points;
        this.minTiles = minTiles;
        this.minGroups = minGroups;
    }
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return (bookshelf) -> {
            Integer groupsFound = 0;
            while (findAndDeleteGroup(bookshelf, minTiles)) groupsFound++;
            return groupsFound >= this.minGroups ? this.points * groupsFound : 0;
        };
    }
    private boolean findAndDeleteGroup(List<List<Optional<Tile>>> bookshelf, Integer minTiles) {
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
    private void removeTiles(List<List<Optional<Tile>>> bookshelf, List<List<Boolean>> mask) {
        for (int i = 0; i < mask.size(); i++) {
            for (int j = 0; j < mask.get(i).size(); j++) {
                if (mask.get(i).get(j)) {
                    bookshelf.get(i).set(j, Optional.empty());
                }
            }
        }
    }
    private Integer countAdjacentTiles(List<List<Boolean>> mask) {
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
    private void markAdjacentTiles(List<List<Optional<Tile>>> bookshelf, Tile checkedTile, List<List<Boolean>> mask) {
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
