package it.polimi.ingsw.client.communication;

/**
 * this interface is a simple interface for opening or closing a generic type of connection
 * the client MUST use only one instance of this class
 */
public interface ClientConnection {
    public void openConnection() throws ClientConnectionException;
    public void closeConnection() throws ClientConnectionException;
}

