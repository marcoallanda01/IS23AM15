package it.polimi.ingsw.server.controller.exceptions;

/**
 * Exception used when a nickname was already taken
 */
public class NicknameTakenException extends Exception{

    /**
     * NicknameException constructor. Use when a nickname was already taken.
     */
    public NicknameTakenException(){
        super();
        System.err.println(this +"Nickname is already taken, can not connect!");
    }

    /**
     * NicknameException constructor. Use when game with a nickname was already taken.
     * @param player nickname tried
     */
    public NicknameTakenException(String player){
        super();
        System.err.println(this +player+" tried to join!");
    }

    @Override
    public String toString() {
        return "NicknameTakenException:";
    }

}
