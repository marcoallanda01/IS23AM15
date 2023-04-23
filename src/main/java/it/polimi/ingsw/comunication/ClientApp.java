package it.polimi.ingsw.comunication;

import it.polimi.ingsw.client.CLIView;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.ViewController;

public class ClientApp {
    private ServerController serverController;
    private ViewController viewController;
    private View view;
    public  ClientApp(String settings) {
        if (settings == "RMI CLI") {
            try {
                RMIClientApp rmiClientApp = new RMIClientApp();
                this.view = new CLIView();
                this.serverController = new RMIServerController(rmiClientApp);
                // controller needs a reference to the serverController to send messages
                this.viewController = new ViewController(this.view, this.serverController);
                // client needs a reference to the viewController to call its methods
                rmiClientApp.setViewController(this.viewController);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.serverController = new TCPServerController(new TCPServer(), new NotificationHandler());
        }
    }

    public static void main(String args[])  //static method
    {
        System.out.println("Starting client app");
        new ClientApp("RMI CLI");
    }
}
