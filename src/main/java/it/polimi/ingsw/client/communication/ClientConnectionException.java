package it.polimi.ingsw.client.communication;

public class ClientConnectionException extends RuntimeException{

    public ClientConnectionException(){
        System.err.println("ClientConnectionException occurred");
    }

    public ClientConnectionException(String message) {
        super(message);
    }

    public ClientConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "ClientConnectionException";
    }
}
