package it.polimi.ingsw.client.communication;


public class ClientCommunicationException extends RuntimeException{

    public ClientCommunicationException(){
        System.err.println("ClientCommunicationException occurred");
    }

    public ClientCommunicationException(String message) {
        super(message);
    }

    public ClientCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ClientCommunicationException";
    }
}
