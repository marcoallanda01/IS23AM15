package it.polimi.ingsw.comunication;

import it.polimi.ingsw.client.CLIView;
import it.polimi.ingsw.client.ViewController;
import it.polimi.ingsw.server.controller.ControllerProvider;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.Game;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServerApp {
    private ClientController clientController;
    private Lobby lobby;
    private ControllerProvider controllerProvider;
    public  ServerApp(String settings) {
        if (settings == "RMI CLI") {
            try {
                RMIServer rmiServer = new RMIServerApp();
                this.clientController = new RMIClientController(rmiServer);

                //TODO: THIS PART NEEDS FIXING (read comments)
                this.lobby = new Lobby("");
                // in truth the lobby needs a reference to the clientController
                // further logic
                this.controllerProvider = new ControllerProvider(new Game(new ArrayList<>(), Boolean.TRUE));
                // in truth both the controllers (and thus the controllerProvider NEED a reference to the clientController)

                // server needs a reference to the lobby to call its methods
                rmiServer.setLobby(this.lobby);
                // server needs a reference to the play controller to call its methods
                rmiServer.setPlayController(this.controllerProvider.getPlayController());
                // server needs a reference to the chat controller to call its methods
                rmiServer.setChatController(this.controllerProvider.getChatController());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.clientController = new TCPClientController(new TCPServer(), new NotificationHandler());
        }
    }
}
