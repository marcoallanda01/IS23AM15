package it.polimi.ingsw.server.communication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("Starting server...");

        ExecutorService executorService = Executors.newCachedThreadPool();

        TCPServer serverTcp = new TCPServer(6000);
        executorService.submit(serverTcp::listenForConnections);

    }
}
