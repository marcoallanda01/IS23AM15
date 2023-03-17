package it.polimi.ingsw.modules;

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
}
