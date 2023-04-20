package it.polimi.ingsw.server.model;

public enum TileType {
    CAT,
    BOOK,
    GAME,
    FRAME,
    TROPHY,
    PLANT;

    public static TileType getRandomTileType() {
        return values()[(int) (Math.random() * values().length)];
    }

    public int getNumberOfTilesPerType() {
        return 22;
    }

    public String getSymbol() {
        return switch (this) {
            case CAT -> "🐱";
            case BOOK -> "📚";
            case GAME -> "🎲";
            case FRAME -> "🖼️";
            case TROPHY -> "🏆";
            case PLANT -> "🌱";
        };
    }

    /**
     * @param name name of the tile type you want to create
     * @return null if there is not a tile type with that name, else a tile type with that name
     */
    public static TileType tileTypeFromName(String name){
        for(TileType tt : TileType.values()){
            if(tt.name().equals(name)){ return tt;}
        }
        return null;
    }
}
