package it.polimi.ingsw.client.communication;

/**
 * this interface is a simple interface for opening or closing a generic type of connection
 * the client MUST use only one instance of this class
 */
public interface ClientConnection {
    /**
     * Opens the connection
     * @throws ClientConnectionException in case of failure
     */
    void openConnection() throws ClientConnectionException;
    /**
     * Closes the connection
     * @throws ClientConnectionException in case of failure
     */
    void closeConnection() throws ClientConnectionException;
}

