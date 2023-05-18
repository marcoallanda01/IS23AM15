package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.communication.*;
import it.polimi.ingsw.client.communication.ClientCommunication;

public class Client {
    private static Client singleton;
    private ClientStates state;
    private View view;
    private ClientNotificationListener clientController;
    private final String hostname;
    private final int port;
    private String id;
    private ClientConnection clientConnection;
    private ClientCommunication clientCommunication;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.id = "NoId";
        state = ClientStates.LOGIN;
    }

    public static Client getInstance() {
        return singleton;
    }

    public ClientConnection getConnection() {
        return clientConnection;
    }

    public void setupNetworkRMI() throws RuntimeException {
        try {
            System.out.println("RMI setup");
            RMIClientConnection rmiClientConnection = new RMIClientConnection(hostname, port, clientController);
            singleton.clientConnection = rmiClientConnection;
            singleton.clientCommunication = new RMIClientCommunication(rmiClientConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setupNetworkTCP() {
        try {
            System.out.println("TCP setup");
            TCPClientConnection tcpClientConnection = new TCPClientConnection(hostname, port, clientController);
            singleton.clientConnection = tcpClientConnection;
            singleton.clientCommunication = new TCPClientCommunication(tcpClientConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)  //static method
    {
        String hostname = parseArg(args, "-a", "--address");
        String port = parseArg(args, "-p", "--port");
        Protocols protocol = parseProtocol(args);
        Views view = parseView(args);
        Modes modes = parseMode(args);

        try {
            singleton = new Client(hostname == null ? "localhost" : hostname, port == null ? 6000 : Integer.parseInt(port));
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            if (modes.equals(Modes.TESTING)) {
                // I am in testing, setup is easier
                TestingView testingView = new TestingView();
                singleton.clientController = testingView;
                if (protocol.equals(Protocols.RMI)) {
                    singleton.setupNetworkRMI();
                } else if (protocol.equals(Protocols.TCP)) {
                    singleton.setupNetworkTCP();
                }
                singleton.clientConnection.openConnection();
                testingView.setClientCommunication(singleton.clientCommunication);
                testingView.start();
            } else {
                if (view.equals(Views.CLI)) {
                    singleton.view = new CLI();
                } else if (view.equals(Views.GUI)) {
                    //singleton.view = new GUI();
                }
                singleton.clientController = new ClientController();
                if (protocol.equals(Protocols.RMI)) {
                    singleton.setupNetworkRMI();
                } else if (protocol.equals(Protocols.TCP)) {
                    singleton.setupNetworkTCP();
                }
            }
            singleton.clientConnection.openConnection();
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

    private static Modes parseMode(String[] args) {
        for (String s : args) {
            if (s.contains("testing")) return Modes.TESTING;
            if (s.contains("production")) return Modes.PRODUCTION;
        }
        return Modes.TESTING; // default
    }

    private static boolean isTesting(String[] args) {
        for (String s : args) {
            if (s.contains("testing")) return true;
        }
        return false; // default
    }

    public View getView() {
        return view;
    }

    private enum Protocols {
        RMI, TCP
    }

    private enum Views {
        CLI, GUI
    }

    private enum Modes {
        TESTING, PRODUCTION
    }

    public ClientStates getClientState() {
        return state;
    }

    public void setClientState(ClientStates state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public ClientController getClientController() {
        if (clientController instanceof ClientController)
            return (ClientController) clientController;
        else
            throw new RuntimeException("ClientController is not an instance of ClientController");
    }

    public ClientCommunication getClientCommunication() {
        return clientCommunication;
    }
}
