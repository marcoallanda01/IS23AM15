package it.polimi.ingsw.client.communication;

/**
 * Class that represents an exception thrown in the ClientCommunication
 */
public class ClientCommunicationException extends RuntimeException{

    /**
     * Creates a runtime exception with null as its detail message
     */
    public ClientCommunicationException(){
        super();
    }

    /**
     * Creates a runtime exception with message as its detail message
     * @param message the message
     */
    public ClientCommunicationException(String message) {
        super(message);
    }

    /**
     * Creates a runtime exception with message as its detail message and cause as the cause
     * @param message the message
     * @param cause the cause
     */
    public ClientCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
