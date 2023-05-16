package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.responses.GameSaved;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.communication.responses.SavedGames;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.server.controller.Lobby;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class is used to implement the ResponseServer necessary to the RMIServerApp
 */
public class RMIRespondServer extends ResponseServer{

    protected Map<RMIClient, String> playersIds;

    public RMIRespondServer(Lobby lobby, String sharedLock, Map<RMIClient, String> playersIds){
        super(lobby, sharedLock);
        this.playersIds = playersIds;
    }

    /**
     * Method for add a playing client
     *
     * @param client client socket
     */
    private synchronized void addPlayingClient(RMIClient client, String id) {
        playersIds.put(client, id);
    }

    /**
     * Method for close a client
     *
     * @param client client
     */
    private void closeClient(RMIClient client) {
        this.playersIds.remove(client);
        try {
            UnicastRemoteObject.unexportObject(client, true);
        } catch (IOException b) {
            b.printStackTrace();
        }
    }

    /**
     * Notify all clients that game have been saved
     *
     * @param name name of the save
     */
    @Override
    protected void notifyGameSaved(String name) {
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyGameSaved(name);
            } catch (RemoteException e) {
                System.err.println("RMI notifyGameSaved: Remote Exception thrown with client " + value);
            }
        });
    }

    /**
     * Close a client connection
     *
     * @param client client object. Cast to RMIClient
     */
    @Override
    protected void closeClient(Object client) {
        closeClient((RMIClient) client);
    }

    /**
     * Get name of a playing client form his connection
     *
     * @param client client object. Cast to RMIClient
     * @return client's nickname. Return null if client is not in game
     */
    @Override
    protected String getPlayerNameFromClient(Object client) {
        RMIClient rmic = (RMIClient)client;
        return this.lobby.getNameFromId(this.playersIds.get(rmic));
    }

    /**
     * Add a playing client
     *
     * @param client object (cast to RMIClient)
     * @param id     player's id
     */
    @Override
    protected void addPlayingClient(Object client, String id) {
        addPlayingClient((RMIClient) client, id);
    }

    /**
     * Ping a client to keep connection alive
     *
     * @param client object (Cast to RMIClient)
     */
    @Override
    protected void ping(Object client) {
        try {
            ((RMIClient)client).notifyPing();
        } catch (RemoteException e) {
            System.err.println("RMI ping: Remote Exception thrown with client " + this.playersIds.get(client));
        }
    }

    protected void gameSetUp(){
        this.playersIds.forEach((key, value) -> {
            try {
                key.notifyGame(new GameSetUp(
                        playController.getPlayers(),
                        new ArrayList<>(playController.getEndGameGoals())
                ));
            } catch (RemoteException e) {
                System.err.println("RMI gameSetUp: Remote Exception thrown with client " + value);
            }
        });
    }
}
