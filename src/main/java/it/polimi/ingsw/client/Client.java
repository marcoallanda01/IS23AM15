package it.polimi.ingsw.client;

import it.polimi.ingsw.client.communication.*;
import it.polimi.ingsw.client.communication.ClientCommunication;

public class Client {
    private static Client singleton;
    private View view;
    private ClientNotificationListener clientNotificationListener;
    private String hostname;
    private int port;
    private ClientConnection clientConnection;
    private ClientCommunication clientCommunication;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static Client getInstance() {
        return singleton;
    }

    public ClientConnection getConnection() {
        return clientConnection;
    }

    public void setupNetworkRMI() throws Exception {
        try {
            RMIClientClientConnection rmiClientConnection = new RMIClientClientConnection(hostname, port, clientNotificationListener);
            this.clientConnection = rmiClientConnection;
            this.clientCommunication = new RMIClientCommunication(rmiClientConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setupNetworkTCP() {
        try {
            TCPClientClientConnection tcpClientConnection = new TCPClientClientConnection(hostname, port, clientNotificationListener);
            this.clientConnection = tcpClientConnection;
            this.clientCommunication = new TCPClientCommunication(tcpClientConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[])  //static method
    {
        String hostname = parseArg(args, "-a", "--address");
        String port = parseArg(args, "-p", "--port");
        Protocols protocol = parseProtocol(args);
        Views view = parseView(args);

        try {
            singleton = new Client(hostname == null ? "localhost" : hostname, port == null ? 12345 : Integer.parseInt(port));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            if (protocol.equals(Protocols.RMI)) {
                singleton.setupNetworkRMI();
            } else if (protocol.equals(Protocols.TCP)) {
                singleton.setupNetworkTCP();
            }

            if (view.equals(Views.CLI)) {
                singleton.view = new CLI(singleton.clientCommunication);
            } else if (view.equals(Views.GUI)) {
                //singleton.view = new GUI();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String parseArg(String[] args, String option, String optionVerbose) {
        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase(option) || arg.equalsIgnoreCase(optionVerbose)) return args[i + 1];
        }
        return null;
    }

    private static Protocols parseProtocol(String[] args) {
        for (String s : args) {
            if (s.contains("rmi")) return Protocols.RMI;
            if (s.contains("tcp")) return Protocols.TCP;
        }
        return Protocols.RMI; // default
    }

    private static Views parseView(String[] args) {
        for (String s : args) {
            if (s.contains("cli")) return Views.CLI;
            if (s.contains("gui")) return Views.GUI;
        }
        return Views.CLI; // default
    }

    private enum Protocols {
        RMI, TCP
    }

    private enum Views {
        CLI, GUI
    }
}
