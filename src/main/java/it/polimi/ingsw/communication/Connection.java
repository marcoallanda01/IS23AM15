package it.polimi.ingsw.communication;

public interface Connection {
    public void openConnection() throws Exception;

    public void closeConnection() throws Exception;
}

