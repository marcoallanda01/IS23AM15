package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.communication.rmi.RMIClient;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.exceptions.PlayerNotFoundException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used to implement the ResponseServer necessary to the RMIServerApp
 */
public class RMIRespondServer extends ResponseServer{

    protected final Map<RMIClient, String> playersIds;

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
        } catch (RemoteException | RuntimeException e) {
            System.err.println("RMI ping: Remote Exception thrown with client " + this.playersIds.get(client));
        }
    }

    /**
     * Send an error message to a player
     *
     * @param player  player's id
     * @param message Error message
     */
    @Override
    protected void sendErrorMessage(String player, String message) {
        synchronized (playersIds){
            this.playersIds.forEach((key, value) -> {
                if(value.equals(player)) {
                    try {
                        key.notifyError(message);
                    } catch (RemoteException | RuntimeException e) {
                        System.err.println("RMI sendErrorMessage: Remote Exception thrown with client " + value);
                    }
                }
            });
        }
    }

    /**
     * Send an error message to all players
     *
     * @param message Error message
     */
    @Override
    protected void sendErrorMessageToAll(String message) {
        synchronized (playersIds) {
            this.playersIds.forEach((key, value) -> {
                try {
                    key.notifyError(message);
                } catch (RemoteException | RuntimeException e) {
                    System.err.println("RMI sendErrorMessageToAll: Remote Exception thrown with client " + value);
                }
            });
        }
    }

    /**
     * Send one GameSetUp object to every player
     */
    protected void gameSetUp(){
        synchronized (playLock) {
            synchronized (playersIds) {
                this.playersIds.forEach((key, value) -> {
                    try {
                        key.notifyGame(new GameSetUp(
                                playController.getPlayers(),
                                new ArrayList<>(playController.getEndGameGoals()),
                                playController.getPersonalGoalCard(getPlayerNameFromClient(key))
                        ));
                    } catch (RemoteException | RuntimeException e) {
                        System.err.println("RMI gameSetUp: Remote Exception thrown with client " + value);
                    } catch (PlayerNotFoundException e) {
                        System.out.println("GameSetUp: This player do not exists " + getPlayerNameFromClient(key));
                    }
                });
            }
        }
    }

    /**
     * Handle the reconnection of a player
     */
    protected void handleReconnection(RMIClient client, String name){
        synchronized (playLock) {
            try {
                client.notifyGame(new GameSetUp(
                        playController.getPlayers(),
                        new ArrayList<>(playController.getEndGameGoals()),
                        playController.getPersonalGoalCard(name)
                ));
                client.notifyCommonGoalCards(playController.getCommonGoalCardsToTokens());
                client.notifyBoard(playController.getBoard());
            } catch (RemoteException | RuntimeException e) {
                System.err.println("RMI handleReconnection: Remote Exception thrown with client " + name);
            } catch (PlayerNotFoundException e) {
                System.out.println("handleReconnection: This player do not exists " + name);
            }
            this.playersIds.forEach((c, id) -> {
                try {
                    if (!Objects.equals(c, client)) {
                        String thisPlayer = lobby.getNameFromId(id);
                        client.notifyBookshelf(thisPlayer, playController.getBookshelf(thisPlayer));
                    }
                } catch (RemoteException | RuntimeException e) {
                    System.err.println("RMI handleReconnection: Remote Exception thrown with client " + name);
                } catch (PlayerNotFoundException e) {
                    System.out.println("handleReconnection:" + e.getMessage());
                }
            });
        }
    }
}
