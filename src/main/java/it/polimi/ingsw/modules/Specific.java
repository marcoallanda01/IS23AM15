package it.polimi.ingsw.modules;

import java.util.*;
import java.util.function.BiConsumer;
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
            List<List<List<Tile>>> validSequences = new ArrayList<>();
            // need to pass it by reference because it is a difficult recursive function which adds the elements while on the leaves
            getPossibleSequencesConsumer().accept(allGroups, validSequences);
            Boolean validSequenceFound = false;
            // we need to check if there is a sequence with enough groups and not too many types across groups.
            // can be done easily with validSequences
            validSequences.stream().filter(sequence -> {})
            if (validSequenceFound) {
                return 1;
            }
            return 0;
        };
    }
    private Predicate<List<List<Tile>>> isSequenceValid() {
        return (sequence) -> {
            Integer groupNumber = groupNum;
            Boolean sameTypeAcrossGroups = this.sgc;
            Boolean enoughGroups = sequence.size() > groupNumber;
            Boolean sameColor = sequence.stream().flatMap(Collection::stream).map(Tile::getType).collect(Collectors.toSet()).size() == 1;
            return enoughGroups && sameColor;
        };
    }
        // gets all the groups, even if overlapping
    private Function<List<List<Optional<Tile>>>, List<List<Tile>>> getAllGroups() {
        return (bookshelf) -> {
            List<List<List<Boolean>>> masks = this.masks; // this.masks
            int maxTypesInGroup = this.minColor;
            int minTypesInGroup = this.maxColor;
            List<List<Tile>> allTheGroups = new ArrayList<>();
            for (int i = 0; i < bookshelf.size() - masks.get(0).size() - 1; i++) {
                for (int j = 0; j < bookshelf.get(i).size() - masks.get(0).get(0).size() - 1; j++) {
                    // if all masks have been checked, go forward
                    for (int k = i; k < masks.size(); k++) {
                        List<List<Boolean>> currentMask = masks.get(k);
                        Boolean isPatternValid = true;
                        Set<TileType> typesInPattern = new HashSet<>();
                        List<Tile> validGroup = new ArrayList<>();
                        for (int l = i; l < currentMask.size() && isPatternValid; l++) {
                            for (int m = i; m < currentMask.get(l).size() && isPatternValid; m++) {
                                // if there should be a tile but there isn't the pattern is not valid, change mask
                                if (currentMask.get(l).get(m) && bookshelf.get(l).get(m).isEmpty()) {
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
                        // too few types in group
                        if (typesInPattern.size() < minTypesInGroup) {
                            isPatternValid = true;
                        }
                        if (isPatternValid) {
                            allTheGroups.add(validGroup);
                        }
                    }
                }
            }
            return allTheGroups;
        };
    }
    // allTheGroups = [[T1, T2, ], [...]] = [Group1, Group2,...]
    // notOverlappingSequences = [[[T1, T4, T5] ...], [...]] = [[Group1, Group4, ...], [...]] = [Sequence1, Sequence2, ...]
    // returns (by reference) a list lists which contain non overlapping groups
    private BiConsumer<List<List<Tile>>, List<List<List<Tile>>>> getPossibleSequencesConsumer() {
        return (startingGroups, notOverlappingSequences) -> {
            BiFunction<List<List<Tile>>, Integer, Boolean> removeOverlappingFunction = this.getRemoveOverlappingFunction();
            for (Integer i = 0; i < startingGroups.size(); i++) {
                List<List<Tile>> toBeFilteredGroups = new ArrayList<>(startingGroups);
                if (removeOverlappingFunction.apply(toBeFilteredGroups, i)) {
                    getPossibleSequencesConsumer().accept(startingGroups, notOverlappingSequences);
                } else {
                    notOverlappingSequences.add(toBeFilteredGroups);
                }
            }
        };
    }
    // removes all groups with any tile overlapping the group at index
    private BiFunction<List<List<Tile>>, Integer, Boolean> getRemoveOverlappingFunction() {
        return (groups, index) -> {
            List<Tile> mainGroup = groups.get(index);
            Boolean groupsChanged = false;
            for (int i = 0; i < mainGroup.size(); i++) {
                Tile tile = mainGroup.get(i);
                List<List<Tile>> overlappingGroups = groups.stream().filter(group -> group.contains(tile)).collect(Collectors.toList());
                groupsChanged = groupsChanged || groups.removeAll(overlappingGroups);
            }
            // returning if any groups have been removed
            return groupsChanged;
        };
    }
}
