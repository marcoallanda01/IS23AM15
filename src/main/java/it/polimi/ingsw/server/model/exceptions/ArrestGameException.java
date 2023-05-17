package it.polimi.ingsw.server.model.exceptions;

/**
 * This exception is used when there is an error in the setting files or in the state of the game that doesn't allow
 * to continue the playing. It extends RuntimeException for avoiding bad practise allowing the game to go on.
 * Bad practise example: catch(Exception e){}
 */
public class ArrestGameException extends RuntimeException{

    public ArrestGameException(){
        System.err.println("ArrestGameException occurred");
    }

    public ArrestGameException(String message) {
        super(message);
    }

    public ArrestGameException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ArrestGameException{}";
    }
}
