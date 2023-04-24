package it.polimi.ingsw.communication;

import it.polimi.ingsw.client.CLIView;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewController;

public class ClientApp {
    private ServerController serverController;
    private ViewController viewController;
    private View view;
    private Connection connection;
    public  ClientApp(String settings) {
        if (settings == "RMI CLI") {
            try {
                RMIClientConnection rmiClientConnection = new RMIClientConnection();
                this.connection = rmiClientConnection;
                this.view = new CLIView();
                this.serverController = new RMIServerController(rmiClientConnection);
                // controller needs a reference to the serverController to send messages
                this.viewController = new ViewController(this.view, this.serverController);
                // client needs a reference to the viewController to call its methods
                rmiClientConnection.setViewController(this.viewController);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            //this.serverController = new TCPServerController(new TCPServer(), new NotificationHandler());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ViewController getViewController() {
        return this.viewController;
    }
    public static void main(String args[])  //static method
    {
        System.out.println("Starting client app");
        new ClientApp("RMI CLI");
    }
}
