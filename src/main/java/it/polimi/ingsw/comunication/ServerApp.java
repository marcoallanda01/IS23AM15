package it.polimi.ingsw.comunication;

import it.polimi.ingsw.server.controller.ControllerProvider;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.Game;

import java.util.ArrayList;
import java.util.List;

public class ServerApp {
    private ClientController clientController;
    private Lobby lobby;
    private ControllerProvider controllerProvider;
    private Connection connection;
    public  ServerApp(String settings) {
        if (settings == "RMI CLI") {
            try {
                RMIServerConnection rmiServerConnection = new RMIServerConnection();
                this.connection = rmiServerConnection;
                this.clientController = new RMIClientController(rmiServerConnection);

                //TODO: THIS PART NEEDS FIXING (read comments)
                this.lobby = new Lobby("");
                // in truth the lobby needs a reference to the clientController
                // further logic
                List<String> nicknames = new ArrayList<>();
                nicknames.add("aaa");
                this.controllerProvider = new ControllerProvider(new Game(nicknames, Boolean.TRUE));
                // in truth both the controllers (and thus the controllerProvider NEED a reference to the clientController)

                // server needs a reference to the lobby to call its methods
                rmiServerConnection.setLobby(this.lobby);
                // server needs a reference to the play controller to call its methods
                rmiServerConnection.setPlayController(this.controllerProvider.getPlayController());
                // server needs a reference to the chat controller to call its methods
                rmiServerConnection.setChatController(this.controllerProvider.getChatController());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.clientController = new TCPClientController(new TCPServer());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ClientController getClientController() {
        return this.clientController;
    }
    public static void main(String args[])  //static method
    {
        System.out.println("Starting client app");
        new ServerApp("RMI CLI");
    }
}
