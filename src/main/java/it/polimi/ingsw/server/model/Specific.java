package it.polimi.ingsw.server.model;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingInt;

public class Specific extends Pattern{
    private final List<List<List<Boolean>>> masks;
    private final boolean sgc;
    private final int minColor;
    private final int maxColor;
    private final int groupNum;
    private final boolean forceEmpty;
    /**
     * @param name the name of the pattern
     * @param masks a list of matrix representing the acceptable shapes of the groups
     * @param groupNum the minimum number of groups to be found to return the points (and not 0)
     * @param sgc should all the groups have the same color?
     * @param minC minimum number of colors in the same group
     * @param maxC maximum number of colors in the same group
     */
    public Specific(String name, List<List<List<Boolean>>> masks, int groupNum, boolean sgc, int minC, int maxC)  throws InvalidPatternParameterException{
        super(name);
        this.sgc = sgc;
        this.minColor = minC;
        this.maxColor = maxC;
        this.groupNum = groupNum;
        this.masks = new ArrayList<>(masks);
        this.forceEmpty = false;
        this.checkParameters();
    }
    /**
     * @param name the name of the pattern
     * @param masks a list of matrix representing the acceptable shapes of the groups
     * @param groupNum the minimum number of groups to be found to return the points (and not 0)
     * @param sgc should all the groups have the same color?
     * @param minC minimum number of colors in the same group
     * @param maxC maximum number of colors in the same group
     * @param fe if set to true, cells set to false in any of the mask will require the corresponding slot in the bookshelf to be empty
     */
    public Specific(String name, List<List<List<Boolean>>> masks, int groupNum, boolean sgc, int minC, int maxC, boolean fe)  throws InvalidPatternParameterException{
        super(name);
        this.sgc = sgc;
        this.minColor = minC;
        this.maxColor = maxC;
        this.groupNum = groupNum;
        this.masks = new ArrayList<>(masks);
        this.forceEmpty = fe;
        this.checkParameters();
    }
    /**
     * Checks the constructor parameters
     * @throws InvalidPatternParameterException if the parameters are invalid
     */
    private void checkParameters()  throws InvalidPatternParameterException{
        if (this.masks == null) {
            throw new InvalidPatternParameterException("masks cannot be null");
        }
        for (int i = 0; i< this.masks.size(); i++) {
            if (this.masks.get(i) == null) {
                throw new InvalidPatternParameterException("masks cannot contain a null element");
            }
        }
        for (int i = 0; i< this.masks.size(); i++) {
            for (int j = 0; j < this.masks.get(i).size(); j++) {
                if (this.masks.get(i).get(j) == null) {
                    throw new InvalidPatternParameterException("masks cannot contain an array containing a null element");
                }
            }
        }
        for (int i = 0; i< this.masks.size(); i++) {
            for (int j = 0; j < this.masks.get(i).size(); j++) {
                for (int k = 0; k < this.masks.get(i).get(j).size(); k++) {
                    if (this.masks.get(i).get(j).get(k) == null) {
                        throw new InvalidPatternParameterException("masks cannot contain a matrix containing a null element");
                    }
                }
            }
        }
        for (int i = 0; i< this.masks.size(); i++) {
            for (int j = 0; j < this.masks.get(i).size(); j++) {
                for (int k = 0; k < this.masks.get(i).get(j).size(); k++) {
                    if (this.masks.get(i).get(j).size() != this.masks.get(i).get(0).size()) {
                        throw new InvalidPatternParameterException("masks cannot contain arrays of different lengths");
                    }
                }
            }
        }
        if (groupNum <= 0) {
            throw new InvalidPatternParameterException("groupNum must be strictly positive");
        }
        if (groupNum > 16) {
            throw new InvalidPatternParameterException("groupNum must be less than or equal to 16");
        }
        if (sgc && (minColor > 1 && maxColor > 1)) {
            throw new InvalidPatternParameterException("if sgc is set to true, both minColor and maxColor must be 1");
        }
        if (minColor > maxColor) {
            throw new InvalidPatternParameterException("minC must be less than or equal to maxColor");
        }
        if (minColor <= 0) {
            throw new InvalidPatternParameterException("minColor must be strictly positive");
        }
        if (maxColor <= 0) {
            throw new InvalidPatternParameterException("maxColor must be strictly positive");
        }
    }
    /**
     * @return a function that given the bookshelf checks if it satisfies the pattern,
     * and if it does, then 1 is returned (that could be interpreted as the number of
     * points or just used to check if it satisfies the pattern), otherwise 0.
     */
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return (bookshelf) -> {
            List<List<Tile>> allGroups = new ArrayList<>(getAllGroups().apply(bookshelf)); // getting all the groups using the private function
            return Boolean.compare(findGoodSequence().test(allGroups), false);
        };
    }
    /**
     * @return a predicate that given sequence of groups, checks if it is a valid sequence according to:
     * same group color (sgc) and the group number (groupNum)
     */
    private Predicate<List<List<Tile>>> isSequenceValid() {
        return (sequence) -> {
            if (!this.sgc) {
                return sequence.size() >= this.groupNum;
            }
            // dividing tile types by number of occurrences (by checking the type of the first tile of each group)
            Map<TileType, Integer> typeToNumberOfGroups = sequence.stream().map(group -> group.get(0).getType()).collect(Collectors.groupingBy(tileType -> tileType, Collectors.summingInt(x -> 1)));
            // finding the max number of groups with the same tile type
            return typeToNumberOfGroups.entrySet().stream().map(entry -> entry.getValue()).mapToInt(Integer::intValue).max().orElse(0) >= this.groupNum;
        };
    }

    /**
     * @return Transposed masks: a list of "matrix" columns x rows
     */
    private List<List<List<Boolean>>> transposeMasks(){
        List<List<List<Boolean>>> tMasks = new ArrayList<List<List<Boolean>>>();

        for(List<List<Boolean>> m : this.masks) {
            List<Integer> sizes = new ArrayList<>();
            for (List<Boolean> r : m) {
                sizes.add(r.size());
            }
            int maxNumCol = Collections.max(sizes);
            List<List<Boolean>> tm = new ArrayList<>();
            for (int j = 0; j < maxNumCol; j++) {
                List<Boolean> col = new ArrayList<>();
                for (List<Boolean> r : m) {
                    try {
                        col.add(r.get(j));
                    }catch (IndexOutOfBoundsException ignored){
                        col.add(false);
                    }
                }
                tm.add(col);
            }
            tMasks.add(tm);
        }

        return tMasks;
    }

    /**
     *
     * @return Transposed masks: a list of "matrix" columns x rows
     */
    public List<List<List<Boolean>>> getTransposedMasks(){
        return transposeMasks();
    }

    /**
     * @returna function that given the bookshelf returns:
     * a list of groups (lists of tiles) representing all the groups that satisfy:
     * at least one of the masks (accounting for force empty),
     * the minimum and maximum amount of different colors
     */
    private Function<List<List<Optional<Tile>>>, List<List<Tile>>> getAllGroups() {
        return (bookshelf) -> {
            List<List<List<Boolean>>> masks = transposeMasks();
            List<List<Tile>> allTheGroups = new ArrayList<>();
            // if all masks have been checked, go forward
            for (int k = 0; k < masks.size(); k++) {
                List<List<Boolean>> currentMask = masks.get(k);
                for (int i = 0; i < bookshelf.size() - masks.get(k).size() + 1; i++) {
                    boolean isPatternValid;
                    for (int j = 0; j < bookshelf.get(i).size() - masks.get(k).get(0).size() + 1; j++) {
                        isPatternValid = true;
                        Set<TileType> typesInPattern = new HashSet<>();
                        List<Tile> validGroup = new ArrayList<>();
                        for (int l = i; l < (currentMask.size() + i) && isPatternValid; l++) {
                            for (int m = j; m < (currentMask.get(l - i).size() + j) && isPatternValid; m++) {
                                // if the mask is false
                                if (currentMask.get(l - i).get(m - j)) {
                                    // if there should be a tile but there isn't go forward
                                    if (bookshelf.get(l).get(m).isEmpty()) {
                                        isPatternValid = false;
                                    } else {
                                        // if the tile is valid store its type
                                        typesInPattern.add(bookshelf.get(l).get(m).get().getType());
                                        // too many types in group
                                        if (typesInPattern.size() > this.maxColor) {
                                            isPatternValid = false;
                                        } else {
                                            // store the tile in the group (down here to avoid useless storing)
                                            validGroup.add(bookshelf.get(l).get(m).get());
                                        }
                                    }
                                } else {
                                    // if there shouldn't be a tile but there is go forward
                                    if (this.forceEmpty && bookshelf.get(l).get(m).isPresent()) {
                                        isPatternValid = false;
                                    }
                                }
                            }
                        }
                        // too few types in group
                        if (typesInPattern.size() < this.minColor) {
                            isPatternValid = false;
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
    /**
     * @returna a predicate that given a sequence of groups returns:
     * true, when:
     * no groups in the sequence have overlapping tiles
     * and the sequence satisfies the predicate returned from isSequenceValid
     */
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
            return !sequenceChanged && isSequenceValid().test(startingGroups);
        };
    }
    /**
     * @returna a bifunction that given the sequence of groups and a mainGroup:
     * removes all groups overlapping with mainGroup from the sequence
     * returns true if any have been removed
     */
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
