package it.polimi.ingsw.server.controller.exceptions;

public class FullGameException extends Exception{

    /**
     * FullGameException constructor. Use when game is full.
     */
    public FullGameException(){
        super();
        System.err.println(toString()+"Game capacity already full, can not connect!");
    }

    /**
     * FullGameException constructor. Use when game is full.
     * @param player nickname of the player who tried to join
     */
    public FullGameException(String player){
        super();
        System.err.println(toString()+player+" tried to join!");
    }

    @Override
    public String toString() {
        return "FullGameException:";
    }

}
