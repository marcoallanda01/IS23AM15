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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is used to implement the ResponseServer necessary to the RMIServerApp
 */
public class RMIRespondServer extends ResponseServer{

    protected final Map<RMIClient, String> playersIds;
    private final ExecutorService executorService;

    /**
     * RMIRespondServer constructor
     * @param lobby lobby object
     * @param sharedLock shared lock between response servers
     * @param playersIds players with their ids
     */
    public RMIRespondServer(Lobby lobby, String sharedLock, Map<RMIClient, String> playersIds){
        super(lobby, sharedLock);
        this.playersIds = playersIds;
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Method for add a playing client
     *
     * @param client client socket
     */
    private synchronized void addPlayingClient(RMIClient client, String id) {
        synchronized (playersIds) {
            playersIds.put(client, id);
        }
    }

    /**
     * Method for close a client
     *
     * @param client client
     */
    private void closeClient(RMIClient client) {
        synchronized (playersIds) {
            this.playersIds.remove(client);
        }
        try {
            UnicastRemoteObject.unexportObject(client, true);
        } catch (IOException b) {
            System.err.println("Player RMI: " + client + " already closed.");
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
        String id;
        synchronized (playersIds){
            id = this.playersIds.get(rmic);
        }
        return this.lobby.getNameFromId(id);
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
            synchronized (playersIds) {
                System.err.println("RMI ping: Remote Exception thrown with client " + this.playersIds.get(client));
            }
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
                executorService.submit( () -> {
                    if (value.equals(player)) {
                        try {
                            key.notifyError(message);
                        } catch (RemoteException | RuntimeException e) {
                            System.err.println("RMI sendErrorMessage: Remote Exception thrown with client " + value);
                        }
                    }
                });
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
                executorService.submit( () -> {
                    try {
                        key.notifyError(message);
                    } catch (RemoteException | RuntimeException e) {
                        System.err.println("RMI sendErrorMessageToAll: Remote Exception thrown with client " + value);
                    }
                });
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
                    String playerName = getPlayerNameFromClient(key);
                    executorService.submit( () -> {
                        try {
                            key.notifyGame(new GameSetUp(
                                    playController.getPlayers(),
                                    new ArrayList<>(playController.getCommonGoals()),
                                    playController.getPersonalGoalCard(playerName),
                                    chatController.getPlayerMessages(playerName)
                            ));
                        } catch (RemoteException | RuntimeException e) {
                            System.err.println("RMI gameSetUp: Remote Exception thrown with client " + value);
                        } catch (PlayerNotFoundException e) {
                            System.out.println("GameSetUp: This player do not exists " + playerName);
                        }
                    });
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
                playController.getPlayers().forEach((pn) ->
                    {
                        try {
                            client.notifyBookshelf(pn, playController.getBookshelf(pn));
                        } catch (PlayerNotFoundException e) {
                            System.err.println("Cannot handle BookShelfUpdate:" + pn
                                    + " reconnection of " + name);
                        } catch (RemoteException | RuntimeException e) {
                            System.err.println("RMI handleReconnection: Remote Exception thrown with client " + name);
                        }
                    }
                );
                playController.getPlayers().forEach((pn) ->
                        {
                            try {
                                client.notifyPoints(pn, playController.getPoints(pn));
                            } catch (PlayerNotFoundException e) {
                                System.err.println("Cannot handle PointsUpdate:" + pn
                                        + " reconnection of " + name);
                            } catch (RemoteException | RuntimeException e) {
                                System.err.println("RMI handleReconnection: Remote Exception thrown with client " + name);
                            }
                        }
                );

                client.notifyCommonCards(playController.getCommonCardsToTokens());
                client.notifyBoard(playController.getBoard());
                client.notifyTurn(playController.getCurrentPlayer());
                client.notifyGame(new GameSetUp(
                        playController.getPlayers(),
                        new ArrayList<>(playController.getCommonGoals()),
                        playController.getPersonalGoalCard(name),
                        chatController.getPlayerMessages(name)
                ));

            } catch (RemoteException | RuntimeException e) {
                System.err.println("RMI handleReconnection: Remote Exception thrown with client " + name);
            } catch (PlayerNotFoundException e) {
                System.out.println("handleReconnection: This player do not exists " + name);
            }
        }
        startPingPong(client, lobby.getIdFromName(name));
    }
}
