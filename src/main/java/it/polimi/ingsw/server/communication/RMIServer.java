package it.polimi.ingsw.server.communication;
import it.polimi.ingsw.server.controller.WaitLobbyException;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
/*
public interface RMIServer extends Remote{
    public Optional<String> hello(RMIClient client) throws RemoteException, WaitLobbyException;


    public List<String> getLoadedGamePlayers();
    public Set<String> getSavedGames();
    public boolean joinFirstPlayer(String name, int numPlayersGame, String id);
    public List<String> loadGame(String name, String idFirstPlayer) throws GameLoadException, GameNameException,
            IllegalLobbyException;

    public boolean joinLoadedGameFirstPlayer(String name, String id) throws NicknameException;

    public boolean joinLoadedGameFirstPlayer(String name, String id, boolean easyRules) throws NicknameException;

    public String join(String player) throws FullGameException, NicknameTakenException, NicknameException;

    public boolean disconnect(String player);

    public boolean isFistPlayerPresent();

    public boolean isReadyToPlay();

    public ControllerProvider startGame() throws EmptyLobbyException;


    class ServerApp {
        private ClientController clientController;
        private Lobby lobby;
        private ControllerProvider controllerProvider;
        private Connection connection;
        public  ServerApp(String settings) {
            if ("RMI CLI".equals(settings)) {
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
            } else if ("TCP CLI".equals(settings)) {
                //this.clientController = new TCPClientController(new TCPServer());
            }
            else {
                throw new RuntimeException("Communication protocol not supported!");
            }
        }

        public Connection getConnection() {
            return connection;
        }
        public ControllerProvider getControllerProvider() {
            return this.controllerProvider;
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
}
*/
