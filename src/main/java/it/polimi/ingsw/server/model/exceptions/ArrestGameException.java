package it.polimi.ingsw.server.model.exceptions;

/**
 * This exception is used when there is an error in the setting files or in the state of the game that doesn't allow
 * to continue the playing. It extends RuntimeException for avoiding bad practise allowing the game to go on.
 * Bad practise example: catch(Exception e){}
 */
public class ArrestGameException extends RuntimeException{

    /**
     * ArrestGameException constructor
     */
    public ArrestGameException(){
        super();
        System.err.println("ArrestGameException occurred");
    }

    /**
     * ArrestGameException constructor
     * @param message exception message
     */
    public ArrestGameException(String message) {
        super(message);
    }

    /**
     * ArrestGameException constructor
     * @param message exception message
     * @param cause exception cause
     */
    public ArrestGameException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ArrestGameException";
    }
}
