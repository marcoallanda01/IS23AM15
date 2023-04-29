package it.polimi.ingsw.server.communication;

import it.polimi.ingsw.server.controller.Lobby;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private static String saves = "saves";
    public static void main(String[] args) {
        System.out.println("Starting server...");


        Lobby lobby = new Lobby(saves);

        ExecutorService executorService = Executors.newCachedThreadPool();

        TCPServer serverTcp = new TCPServer(6000, lobby);
        executorService.submit(serverTcp::listenForConnections);

    }
}
