package it.polimi.ingsw.client.communication;

import it.polimi.ingsw.client.CLIRenderer;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.communication.ClientCommunication;

public class ClientApp {
    private ClientCommunication clientCommunication;
    private View view;
    private Connection connection;
    public  ClientApp(String settings) {
        if (settings == "RMI CLI") {
            try {
                RMIClientConnection rmiClientConnection = new RMIClientConnection();
                this.connection = rmiClientConnection;
                this.clientCommunication = new RMIClientCommunication(rmiClientConnection);
                //this.view = new CLIRenderer(this.clientCommunication);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // TCP SETUP
        }
    }

    public Connection getConnection() {
        return connection;
    }
    public static void main(String args[])  //static method
    {
        System.out.println("Starting client app");
        new ClientApp("RMI CLI");
    }
}
