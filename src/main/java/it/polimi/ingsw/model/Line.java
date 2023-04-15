package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;

public class Line extends Specific{
    private int tilesNum;
    private List<Character> directions;
    /**
     * @param tilesNum length of the line
     * @param directions directions of the line, they can be ('O', 'V', 'D')
     * @param groupNum number of groups that have to be present
     * @param sgc      true if different groups of tails must have same colour
     * @param minC     min number of different colour that have to be present in a group
     * @param maxC     max number of different colour that have to be present in a group
     */
    public Line(String name, int tilesNum, List<Character> directions, int groupNum, boolean sgc, int minC, int maxC) {
        super(name, generateMasks(directions), groupNum, sgc, minC, maxC);
        this.tilesNum = tilesNum;
        this.directions = new ArrayList<Character>(directions);
    }

    private static List<List<List<Boolean>>> generateMasks(List<Character> directions) {
        return new ArrayList<>();
    }
}
