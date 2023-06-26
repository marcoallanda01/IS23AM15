package it.polimi.ingsw.server.controller.exceptions;

/**
 * Exception used  when a nickname is not available
 */
public class NicknameException extends Exception{

    /**
     * NicknameException constructor. Use when a nickname is not available.
     */
    public NicknameException(){
        super();
        System.err.println(this +"Nickname can't be taken, can not connect!");
    }

    /**
     * NicknameException constructor. Use when game with a nickname is not available.
     * @param player nickname tried
     */
    public NicknameException(String player){
        super();
        System.err.println(this +player+" tried to join!");
    }

    @Override
    public String toString() {
        return "NicknameException:";
    }

}
