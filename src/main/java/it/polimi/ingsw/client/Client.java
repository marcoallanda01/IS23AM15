package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.communication.*;
import it.polimi.ingsw.client.communication.ClientCommunication;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GUIApplication;
import javafx.application.Application;

import java.io.*;

public class Client {
    private static Client singleton;
    private ClientStates state;
    private View view;
    private ClientNotificationListener clientController;
    private final String hostname;
    private final int port;
    private final Protocols protocolSetting;
    private final Views viewSetting;
    private final Modes modeSetting;
    private String id;
    private String nickname;
    private ClientConnection clientConnection;
    private ClientCommunication clientCommunication;

    public Client(String hostname, int port, Protocols protocol, Views view, Modes mode) {
        this.hostname = hostname;
        this.port = port;
        loadClientInfo();
        this.protocolSetting = protocol;
        this.viewSetting = view;
        this.modeSetting = mode;
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
        Modes mode = parseMode(args);

        try {
            singleton = new Client(hostname == null ? "localhost" : hostname, port == null ? 6000 : Integer.parseInt(port), protocol, view, mode);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            if (mode.equals(Modes.TESTING)) {
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
                    Client.getInstance().init(new CLI());
                } else if (view.equals(Views.GUI)) {
                    Application.launch(GUIApplication.class, args);
                }
            }
            Client.getInstance().getClientCommunication().reconnect(Client.getInstance().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         Thread shutdownHook = new Thread() {
            @Override
            public void run() {
                System.out.println("Performing shutdown");
                Client.getInstance().getClientController().logout();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
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
        saveClientInfo(id, nickname);
    }

    public String getId() {
        return this.id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void saveClientInfo(String id, String name) {
        try {
            File clientIdFile = new File("client_info.txt");
            FileWriter fileWriter = new FileWriter(clientIdFile);
            fileWriter.write(id);
            if (name != null) {
                fileWriter.write("\n");
                fileWriter.write(name);
            }
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error while saving client ID");
            e.printStackTrace();
        }
    }

    private void loadClientInfo() {
        String id = "NoId";
        String name = "NoName";
        try {
            File clientIdFile = new File("client_info.txt");
            if (clientIdFile.exists()) {
                FileReader fileReader = new FileReader(clientIdFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                id = bufferedReader.readLine();
                name = bufferedReader.readLine();
                bufferedReader.close();
            }
        } catch (IOException e) {
            System.err.println("Error while loading client ID");
            e.printStackTrace();
        }
        this.id = id;
        this.nickname = name;
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

    public void init(View view) {
        this.view = view;
        // clientController must be set before setting up the network, because the connection needs a reference to it
        singleton.clientController = new ClientController();
        if (protocolSetting.equals(Protocols.RMI)) {
            singleton.setupNetworkRMI();
        } else if (protocolSetting.equals(Protocols.TCP)) {
            singleton.setupNetworkTCP();
        }
        try {
            singleton.clientConnection.openConnection();
        } catch (ClientConnectionException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
