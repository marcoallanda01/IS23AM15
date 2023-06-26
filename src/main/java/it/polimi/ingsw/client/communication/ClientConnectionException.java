package it.polimi.ingsw.client.communication;

/**
 * Class that represents an exception thrown in the ClientCommunication
 */
public class ClientConnectionException extends RuntimeException{

    /**
     * Creates a runtime exception with null as its detail message
     */
    public ClientConnectionException(){
        super();
    }

    /**
     * Creates a runtime exception with message as its detail message
     * @param message the message
     */
    public ClientConnectionException(String message) {
        super(message);
    }

    /**
     * Creates a runtime exception with message as its detail message and cause as the cause
     * @param message the message
     * @param cause the cause
     */
    public ClientConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
