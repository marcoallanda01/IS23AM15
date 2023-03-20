package it.polimi.ingsw.modules;

import java.util.ArrayList;

public class Specific extends Pattern{
    private List< List<List<Boolean>> > masks;
    private boolean sgc;
    private boolean stc;
    private int minColor;
    private int maxColor;
    private final int groupNum;

    /**
     * @param mask mask that specifies the pattern true block, false void
     * @param groupNum number of groups that have to be present
     * @param sgc      true if different groups of tails must have same colour
     * @param stc      true if same tails in a group must have same colour
     * @param minC min number of different colour that have to be present in a group
     * @param maxC max number of different colour that have to be present in a group
     */
    public Specific(List<List<Boolean>> masks, int groupNum, boolean sgc, boolean stc, int minC, int maxC) {
        this.sgc = sgc;
        this.stc = stc;
        this.minColor = minC;
        this.maxColor = maxC;
        this.groupNum = groupNum;
        this.masks = new ArrayList< List<List<Boolean>> >(masks);
    }

    // TODO: to finish
    protected Boolean findSinglePath(List<List<Optional<Tile>>> board, int x, int y){
        List<List<Boolean>> mask = masks.get(0);
        for(int rm = 0; rm < mask.size(); rm++){
            List<Optional<Tile>> line = board.get(x+rm);
            List<Boolean> lineM = mask.get(rm);
            for(int cm = 0; cm < mask.get(rm).size(); cm++){
                if(!line.get(y+cm).isPresent() && !lineM.get(cm)){
                    return false;
                }
                else{
                    return true;
                }
            }
        }
    }

    // TODO: to finish
    @java.lang.Override
    public Function< List<List<Optional<Tile>>>, Boolean > getPatternFunction() {
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

    public int getGroupNum() {
        return groupNum;
    }

    public List<List<Boolean>> getMasks(){
        return new ArrayList<List<List<Boolean>>>(masks);
    }

    public void addMask(List<List<Boolean>> mask){
        masks.add(mask);
    }
}
