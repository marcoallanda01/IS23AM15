package it.polimi.ingsw.server;

import it.polimi.ingsw.server.communication.ServerCommunication;
import it.polimi.ingsw.server.communication.TCPServer;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class ServerApp {
    private static String saves = "saves";
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting server...");

        List<ServerCommunication> servers = new ArrayList<>();
        PushNotificationController pushNotificationController = new PushNotificationController(servers);

        Game game = new Game(pushNotificationController);
        Lobby lobby = new Lobby(saves, game);

        ExecutorService executorService = Executors.newCachedThreadPool();
        TCPServer serverTcp = new TCPServer(6000, lobby);
        servers.add(serverTcp);

        do{

            // start tcp server
            executorService.submit(serverTcp::listenForConnections);

            do {
                sleep(3000);
            } while (game.getWinner() == null);

            // Winner is sent and game is ended
            servers.forEach(ServerCommunication::sendWinner);
            // game reset
            game = new Game(pushNotificationController);
            lobby.reset(game);

        }while (true);
    }
}
