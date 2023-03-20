package it.polimi.ingsw.modules;

public class Personal extends Pattern{
    List<Tile> tiles = null;

    public Personal(List<Tile> tiles) {
        super(false, true, 1, 1);
        this.tiles = new ArrayList<Tile>(tiles);
    }

    /**
     *
     * @return function that returns number of tiles in the correct position
     */
    @java.lang.Override
    public Function<List<List<Optional<Tile>>>, Integer> getPatternFunction() {
        return new Function<List<List<Optional<Tile>>>, Integer>() {
            @Override
            public Integer apply(List<List<Optional<Tile>>> board){

                List<Tile> boardList = board.stream()
                        .flatMap(l -> l.stream())
                        .map((o) -> {if(o != null){
                            if(o.isPresent()){return o.get();}
                        }})
                        .collect(Collectors.toList());
                Integer corrects = 0;
                for(int i = 0; i < tiles.size(); i++){
                    if(boardList.constains(tiles.get(i)))
                        corrects++;
                }
                return corrects;
            }
        };
    }
}
