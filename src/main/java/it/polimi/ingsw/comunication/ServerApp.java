package it.polimi.ingsw.comunication;

import it.polimi.ingsw.client.CLIView;
import it.polimi.ingsw.client.ViewController;
import it.polimi.ingsw.server.controller.ControllerProvider;
import it.polimi.ingsw.server.controller.Lobby;
import it.polimi.ingsw.server.model.Game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerApp {
    private ClientController clientController;
    private Lobby lobby;
    private ControllerProvider controllerProvider;
    public  ServerApp(String settings) {
        if (settings == "RMI CLI") {
            try {
                RMIServerApp rmiServerApp = new RMIServerApp();
                this.clientController = new RMIClientController(rmiServerApp);

                //TODO: THIS PART NEEDS FIXING (read comments)
                this.lobby = new Lobby("");
                // in truth the lobby needs a reference to the clientController
                // further logic
                List<String> nicknames = new ArrayList<>();
                nicknames.add("aaa");
                this.controllerProvider = new ControllerProvider(new Game(nicknames, Boolean.TRUE));
                // in truth both the controllers (and thus the controllerProvider NEED a reference to the clientController)

                // server needs a reference to the lobby to call its methods
                rmiServerApp.setLobby(this.lobby);
                // server needs a reference to the play controller to call its methods
                rmiServerApp.setPlayController(this.controllerProvider.getPlayController());
                // server needs a reference to the chat controller to call its methods
                rmiServerApp.setChatController(this.controllerProvider.getChatController());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.clientController = new TCPClientController(new TCPServer(), new NotificationHandler());
        }
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
