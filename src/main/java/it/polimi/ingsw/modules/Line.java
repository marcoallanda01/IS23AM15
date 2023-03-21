package it.polimi.ingsw.modules;

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
     * @param stc      true if same tails in a group must have same colour
     * @param minC     min number of different colour that have to be present in a group
     * @param maxC     max number of different colour that have to be present in a group
     */
    public Line(String name, int tilesNum, List<Character> directions, int groupNum, boolean sgc, int minC, int maxC) {
        super();
        List<List<List<Boolean>>> masks = null;
        //super(name, masks, groupNum, sgc, minC, maxC);
        this.tilesNum = tilesNum;
        this.directions = new ArrayList<Character>(directions);
    }

    @java.lang.Override
    public Function<List<List<Optional<Tile>>>, Boolean> getPatternFunction() {
        return new Function<List<List<Optional<Tile>>>, Boolean>() {
            @Override
            public Boolean apply(List<List<Optional<Tile>>> board){
                List<List<Boolean>> mask = masks.get(0);

                for(int i = 0; i < board.size(); i++){
                    for(int j = 0; j < board.get(i).size(); j++){
                        findSinglePath(board, i, j);
                    }
                }

                return true;
            }
        };
    }
}
