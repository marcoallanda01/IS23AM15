package it.polimi.ingsw.server;

import it.polimi.ingsw.server.communication.TCPServer;
import it.polimi.ingsw.server.controller.Lobby;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private static String saves = "saves";
    public static void main(String[] args){
        System.out.println("Starting server...");

        String sharedLock = "playLock";

        Lobby lobby = new Lobby(saves);

        ExecutorService executorService = Executors.newCachedThreadPool();

        TCPServer serverTcp = new TCPServer(6000, lobby, sharedLock);
        lobby.registerServer(serverTcp);
        executorService.submit(serverTcp::listenForConnections);

    }
}
