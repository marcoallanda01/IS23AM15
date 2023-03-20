package it.polimi.ingsw.modules;

public abstract class Pattern {
    /**
     * @return function that find that type of pattern
     */
    public abstract Function<T, S> getPatternFunction();

}
