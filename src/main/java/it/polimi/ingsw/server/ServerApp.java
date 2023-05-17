package it.polimi.ingsw.server;

import it.polimi.ingsw.server.communication.RMIServerApp;
import it.polimi.ingsw.server.communication.TCPServer;
import it.polimi.ingsw.server.controller.Lobby;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private static String saves = "saves";

    public static void main(String[] args) {
        System.out.println("Starting server...");

        String stringLock = "playLock";
        String sharedLock = stringLock.intern();

        Lobby lobby = new Lobby(saves);

        ExecutorService executorService = Executors.newCachedThreadPool();

        TCPServer serverTcp = new TCPServer(6000, lobby, sharedLock);
        lobby.registerServer(serverTcp);
        executorService.submit(serverTcp::listenForConnections);

        try {
            RMIServerApp serverRMI = new RMIServerApp(7001, lobby, sharedLock);
            serverRMI.start();
            lobby.registerServer(serverRMI);
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("RMI server failed to start!");
            e.printStackTrace();
        }
    }
}
