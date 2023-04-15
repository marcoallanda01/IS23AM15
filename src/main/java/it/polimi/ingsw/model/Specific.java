package it.polimi.ingsw.model;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Specific extends Pattern{
    private List<List<List<Boolean>>> masks;
    private boolean sgc;
    private int minColor;
    private int maxColor;
    private final int groupNum;

    /**
     * @param masks mask that specifies the pattern true block, false void
     * @param groupNum number of groups that have to be present
     * @param sgc      true if different groups of tails must have same colour
     * @param minC min number of different colour that have to be present in a group
     * @param maxC max number of different colour that have to be present in a group
     */
    public Specific(String name, List<List<List<Boolean>>> masks, int groupNum, boolean sgc, int minC, int maxC) {
        super(name);
        this.sgc = sgc;
        this.minColor = minC;
        this.maxColor = maxC;
        this.groupNum = groupNum;
        this.masks = new ArrayList<>(masks);
    }

    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return (bookshelf) -> {
            List<List<Tile>> allGroups = new ArrayList<>(getAllGroups().apply(bookshelf)); // getting all the groups using the private function
            return Boolean.compare(findGoodSequence().test(allGroups), false);
        };
    }
    private Predicate<List<List<Tile>>> isSequenceValid() {
        return (sequence) -> {
            int groupNumber = groupNum;
            boolean sameTypeAcrossGroups = this.sgc;
            boolean enoughGroups = sequence.size() >= groupNumber;
            boolean sameColor = sequence.stream().flatMap(Collection::stream).map(Tile::getType).collect(Collectors.toSet()).size() == 1;
            return enoughGroups && (sameColor || !sameTypeAcrossGroups);
        };
    }
    // gets all the groups, even if overlapping
    private Function<List<List<Optional<Tile>>>, List<List<Tile>>> getAllGroups() {
        return (bookshelf) -> {
            List<List<List<Boolean>>> masks = this.masks; // this.masks
            int maxTypesInGroup = this.maxColor;
            int minTypesInGroup = this.minColor;
            List<List<Tile>> allTheGroups = new ArrayList<>();
            // if all masks have been checked, go forward
            for (int k = 0; k < masks.size(); k++) {
                List<List<Boolean>> currentMask = masks.get(k);
                for (int i = 0; i < bookshelf.size() - masks.get(k).size() + 1; i++) {
                    for (int j = 0; j < bookshelf.get(i).size() - masks.get(k).get(0).size() + 1; j++) {
                        boolean isPatternValid = true;
                        Set<TileType> typesInPattern = new HashSet<>();
                        List<Tile> validGroup = new ArrayList<>();
                        for (int l = i; l < (currentMask.size() + i) && isPatternValid; l++) {
                            for (int m = j; m < (currentMask.get(l - i).size() + j) && isPatternValid; m++) {
                                // if there should be a tile but there isn't the pattern is not valid, change mask
                                if (currentMask.get(l - i).get(m - j)) {
                                    if (bookshelf.get(l).get(m).isEmpty()) {
                                        isPatternValid = false;
                                    } else {
                                        // if the tile is valid store its type
                                        typesInPattern.add(bookshelf.get(l).get(m).get().getType());
                                        // too many types in group
                                        if (typesInPattern.size() > maxTypesInGroup) {
                                            isPatternValid = false;
                                        } else {
                                            // store the tile in the group (down here to avoid useless storing)
                                            validGroup.add(bookshelf.get(l).get(m).get());
                                        }
                                    }
                                }
                            }
                        }
                        // too few types in group
                        if (typesInPattern.size() < minTypesInGroup) {
                            isPatternValid = true;
                        }
                        if (isPatternValid) {
                            allTheGroups.add(new ArrayList<>(validGroup));
                        }
                    }
                }
            }
            return allTheGroups;
        };
    }
    // allTheGroups = [[T1, T2, ], [...]] = [Group1, Group2,...]
    // notOverlappingSequences = [[[T1, T4, T5] ...], [...]] = [[Group1, Group4, ...], [...]] = [Sequence1, Sequence2, ...]
    private Predicate<List<List<Tile>>> findGoodSequence() {
        List<List<Tile>> toBeFilteredGroups = new ArrayList<>();
        return startingGroups -> {
            BiFunction<List<List<Tile>>, List<Tile>, Boolean> removeOverlappingFunction = this.removeOverlapping();
            boolean sequenceChanged = false;
            for (List<Tile> mainGroup : startingGroups) {
                toBeFilteredGroups.clear();
                toBeFilteredGroups.addAll(startingGroups);
                if (removeOverlappingFunction.apply(toBeFilteredGroups, mainGroup)) {
                    if (findGoodSequence().test(toBeFilteredGroups)) {
                        return true;
                    }
                    sequenceChanged = true;
                }
            }
            // if !sequenceChanged, we have a non overlapping sequence
            if (!sequenceChanged && isSequenceValid().test(startingGroups)) {
                return true;
            }
            return false;
        };
    }

    private BiFunction<List<List<Tile>>, List<Tile>, Boolean> removeOverlapping() {
        Set<List<Tile>> overlappingGroups = new HashSet<>();
        return (groups, mainGroup) -> {
            for (List<Tile> group : groups) {
                if (group.stream().anyMatch(mainGroup::contains)) {
                    overlappingGroups.add(group);
                }
            }
            overlappingGroups.remove(mainGroup);
            if (!overlappingGroups.isEmpty()) {
                groups.removeAll(overlappingGroups);
                return true;
            }
            return false;
        };
    }
}
