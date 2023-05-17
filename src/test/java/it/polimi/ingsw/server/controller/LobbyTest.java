package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.communication.ServerCommunication;
import it.polimi.ingsw.server.controller.exceptions.*;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    class ServerCommunicationInstance implements ServerCommunication{

        /**
         * Send one GameSetUp object to every player
         */
        @Override
        public void gameSetUp() {

        }

        /**
         * If in game, function notifies the disconnection of a player to all the others
         *
         * @param playerName player that disconnect
         */
        @Override
        public void notifyDisconnection(String playerName) {

        }

        /**
         * Notify to all clients that a player reconnected
         *
         * @param playerName name of the player who reconnected
         */
        @Override
        public void notifyReconnection(String playerName) {

        }

        /**
         * Send a message to all players
         *
         * @param sender  sender's name
         * @param date    date of message creation
         * @param message actual message to be sent
         */
        @Override
        public void notifyMessage(String sender, String date, String message) {

        }

        /**
         * Send a message to all players
         *
         * @param sender   sender's name
         * @param date     date of message creation
         * @param message  actual message to be sent
         * @param receiver receiver's name
         */
        @Override
        public void notifyMessage(String sender, String date, String message, String receiver) {

        }

        /**
         * Notify change in the board to all clients in game
         *
         * @param tiles board
         */
        @Override
        public void notifyChangeBoard(List<Tile> tiles) {

        }

        /**
         * Notify to all clients change in player's bookshelf
         *
         * @param playerName player's name
         * @param tiles      bookshelf
         */
        @Override
        public void notifyChangeBookShelf(String playerName, List<Tile> tiles) {

        }

        /**
         * Notify change in point of a player to all clients
         *
         * @param playerName player's name
         * @param points     new points
         */
        @Override
        public void updatePlayerPoints(String playerName, int points) {

        }

        /**
         * Notify to all player whom turn is
         *
         * @param playerName current player
         */
        @Override
        public void notifyTurn(String playerName) {

        }

        /**
         * Notify to all clients a change in common goals cards and tokens
         *
         * @param cardsAndTokens cards with associated tokens
         */
        @Override
        public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {

        }

        /**
         * Send notification of the winner to all players
         *
         * @param playerName name of the winner
         */
        @Override
        public void notifyWinner(String playerName) {

        }

        /**
         * Handle the disconnection of the last player terminating the game
         */
        @Override
        public void handleLastPlayerDisconnection() {

        }
    }
    Lobby lobby;
    @BeforeEach
    void initialize(){
        lobby = new Lobby("saves");
    }

    @Test
    void getControllerProvider_null(){
        assertNull(lobby.getControllerProvider());
    }

    @Test
    public void testJoinFirstPlayer_InvalidFirstPlayerId() {
        String name = "Player1";
        int numPlayersGame = 2;
        boolean easyRules = false;
        String id = "654321";

        assertFalse(lobby.joinFirstPlayer(name, numPlayersGame, easyRules, id));
        assertFalse(lobby.getIsCreating());
        assertNull(lobby.getNameFromId(id));
    }

    @Test
    public void testJoinFirstPlayer_InvalidNumPlayersGame() {
        String name = "Player1";
        int numPlayersGame = 5;
        boolean easyRules = false;
        String id = "123456";

        // Higher
        assertFalse(lobby.joinFirstPlayer(name, numPlayersGame, easyRules, id));
        assertFalse(lobby.getIsCreating());
        assertNull(lobby.getNameFromId(id));

        //Lower
        numPlayersGame = 1;
        assertFalse(lobby.joinFirstPlayer(name, numPlayersGame, easyRules, id));
        assertFalse(lobby.getIsCreating());
        assertNull(lobby.getNameFromId(id));
    }


    @Test
    public void testJoinLoadedGameFirstPlayer_InvalidFirstPlayerId() throws NicknameException, WaitLobbyException {
        String name = "Player1";
        String id = "654321";

        assertFalse(lobby.joinLoadedGameFirstPlayer(name, id));
        assertFalse(lobby.getIsCreating());
        assertNull(lobby.getNameFromId(id));

        lobby.join();
        assertFalse(lobby.joinLoadedGameFirstPlayer(name, id));
        assertTrue(lobby.getIsCreating());
        assertNull(lobby.getNameFromId(id));
    }

    @Test
    public void testJoinLoadedGameFirstPlayer_succeed() throws WaitLobbyException, IllegalLobbyException, GameLoadException, GameNameException, NicknameException {
        Optional<String> playerId = lobby.join();
        List<String> loadedPlayers = lobby.loadGame("SaveGameTest", playerId.get());
        lobby.joinLoadedGameFirstPlayer(loadedPlayers.get(0), playerId.get());
        assertFalse(lobby.getIsCreating());
        assertEquals(loadedPlayers.get(0),lobby.getNameFromId(playerId.get()));
    }

    @Test
    public void testJoinLoadedGameFirstPlayer_NicknameException() throws NicknameException, WaitLobbyException, IllegalLobbyException, GameLoadException, GameNameException {
        String name = "aCaso";

        Optional<String> playerId = lobby.join();
        List<String> loadedPlayers = lobby.loadGame("SaveGameTest", playerId.get());
        assertFalse(loadedPlayers.contains(name));
        assertThrows(NicknameException.class, ()->lobby.joinLoadedGameFirstPlayer(name, playerId.get()));
    }

    @Test
    public void testJoinFirstPlayer_succeed() throws WaitLobbyException {
        String name = "Player1";

        String playerId = lobby.join().get();
        lobby.joinFirstPlayer(name, 3, true, playerId);
        assertFalse(lobby.getIsCreating());
        assertEquals(name,lobby.getNameFromId(playerId));
    }

    @Test
    public void testJoinEmptyLobby() throws WaitLobbyException {
        String id = "123456";

        Optional<String> playerId = lobby.join();
        assertTrue(playerId.isPresent());
        assertNotEquals(id, playerId.get());

    }

    @Test
    public void testJoinWhileIsCreating() throws WaitLobbyException {
        lobby.join();
        assertThrows(WaitLobbyException.class, ()->{lobby.join();});
    }

    @Test
    public void testJoinAfterCreationOfGame() throws WaitLobbyException {
        String name = "Player1";

        Optional<String> playerId = lobby.join();
        lobby.joinFirstPlayer(name, 3, false, playerId.get());
        assertEquals(Optional.empty(), lobby.join());
    }

    @Test
    public void testGetNameFromId() throws WaitLobbyException {
        String name = "Player1";
        String id = "123456";

        Optional<String> playerId = lobby.join();
        assertTrue(playerId.isPresent());
        assertNotEquals(id, playerId.get());
        lobby.joinFirstPlayer(name, 3, false, playerId.get());
        //Id present
        assertEquals(name, lobby.getNameFromId(playerId.get()));
        //Null from absent id
        assertNull(lobby.getNameFromId(id));
    }

    @Test
    public void forceResetExecuted() throws WaitLobbyException {
        Optional<String> playerId = lobby.join();
        lobby.removePlayer(playerId.get());
        assertFalse(lobby.getIsCreating());
        assertFalse(lobby.isGameLoaded());
        assertNull(lobby.getNameFromId(playerId.get()));
    }

    @Test
    public void forceResetWhenFinishedToCreate() throws WaitLobbyException, IllegalLobbyException, GameLoadException, GameNameException, NicknameException {
        Optional<String> playerId = lobby.join();
        List<String> loadedPlayers = lobby.loadGame("SaveGameTest", playerId.get());
        lobby.joinLoadedGameFirstPlayer(loadedPlayers.get(0), playerId.get());
        lobby.removePlayer(playerId.get());
        assertFalse(lobby.getIsCreating());
        assertTrue(lobby.isGameLoaded());
        assertNull(lobby.getNameFromId(playerId.get()));
    }

    @Test
    void addPlayerWithoutFirstPlayer() {
        assertThrows(FirstPlayerAbsentException.class, () -> lobby.addPlayer("player"));
    }

    @Test
    void addPlayerSucceed() throws NicknameTakenException, NicknameException, FirstPlayerAbsentException, FullGameException, WaitLobbyException {
        String name = "Player";

        String playerId = lobby.join().get();
        lobby.joinFirstPlayer("FirstPlayer", 3, true, playerId);
        assertNotNull(lobby.addPlayer(name));
    }

    @Test
    void addPlayerNicknameTakenException() throws NicknameTakenException, NicknameException, FirstPlayerAbsentException, FullGameException, WaitLobbyException {
        String name = "Player";

        String playerId = lobby.join().get();
        lobby.joinFirstPlayer("FirstPlayer", 3, true, playerId);
        lobby.addPlayer(name);

        assertThrows(NicknameTakenException.class, ()->lobby.addPlayer(name));
    }

    @Test
    void addPlayerFullGame() throws NicknameTakenException, NicknameException, FirstPlayerAbsentException, FullGameException, WaitLobbyException {
        String name = "Player";

        String playerId = lobby.join().get();
        lobby.joinFirstPlayer("FirstPlayer", 3, true, playerId);
        lobby.addPlayer("1");
        lobby.addPlayer("2");

        assertThrows(FullGameException.class, ()->lobby.addPlayer(name));
    }

    @Test
    void addPlayerNicknameTaken() throws NicknameTakenException, NicknameException, FirstPlayerAbsentException, FullGameException, WaitLobbyException, IllegalLobbyException, GameLoadException, GameNameException {
        Optional<String> playerId = lobby.join();
        List<String> loadedPlayers = lobby.loadGame("SaveGameTest", playerId.get());
        lobby.joinLoadedGameFirstPlayer(loadedPlayers.get(0), playerId.get());

        assertThrows(NicknameTakenException.class, ()->lobby.addPlayer(loadedPlayers.get(0)));
        assertThrows(NicknameException.class, ()->lobby.addPlayer("aCaso"));
    }

    @Test
    void startGameAndIsPlaying() throws WaitLobbyException, NicknameTakenException, NicknameException, FirstPlayerAbsentException, FullGameException, EmptyLobbyException {
        assertFalse(lobby.isPlaying());
        String playerId = lobby.join().get();
        lobby.joinFirstPlayer("FirstPlayer", 3, true, playerId);
        lobby.addPlayer("2");
        assertThrows(EmptyLobbyException.class, ()->lobby.startGame());
        lobby.addPlayer("3");
        assertFalse(lobby.isPlaying());
        lobby.startGame();
        assertTrue(lobby.isPlaying());
    }

    @Test
    void startGameWhenLoaded() throws IllegalLobbyException, GameLoadException, GameNameException, WaitLobbyException, NicknameException, NicknameTakenException, FirstPlayerAbsentException, FullGameException, EmptyLobbyException {
        String playerId = lobby.join().get();
        List<String> playersLoaded =  lobby.loadGame("SaveGameTest", playerId);
        lobby.joinLoadedGameFirstPlayer(playersLoaded.get(0), playerId);
        playersLoaded.remove(playersLoaded.get(0));
        for(String p : playersLoaded){
            lobby.addPlayer(p);
            System.out.println(p);
        }
        assertFalse(lobby.isPlaying());
        assertNotNull(lobby.startGame());
        assertTrue(lobby.isGameLoaded());
        assertTrue(lobby.isPlaying());
    }

    @Test
    void removePlayer() throws WaitLobbyException, NicknameTakenException, NicknameException, FirstPlayerAbsentException, FullGameException {
        String playerId = lobby.join().get();
        lobby.joinFirstPlayer("FirstPlayer", 3, true, playerId);
        lobby.addPlayer("2");
        assertFalse(lobby.removePlayer(null));
        assertFalse(lobby.removePlayer("1"));
        assertTrue(lobby.removePlayer(lobby.getIdFromName("2")));
        assertNull(lobby.getIdFromName("2"));
    }

    @Test
    void getSavedGames() {
        Lobby lobby = new Lobby("/src/main/resources/saves");
        Set<String> savedGames = lobby.getSavedGames();
        File saves = new File("/src/main/resources/saves");
        File[] savesList = saves.listFiles();
        int n = 0;
        if (savesList != null) {
            n = savesList.length;
        }
        //System.out.println(savedGames);
        assertEquals(savedGames.size(), n);

    }

    @Test
    void loadGame() throws GameLoadException, GameNameException, IOException, IllegalLobbyException, WaitLobbyException {
        Game game = new Game(new PushNotificationController(new ArrayList<>()));
        Lobby lobby = new Lobby("saves");
        Optional<String> uniqueID = lobby.join();
        if(uniqueID.isEmpty()){
            fail();
        }
        File saves = new File("saves");
        if (!saves.exists()) {
            System.err.println(saves.getAbsolutePath());
            throw new IOException("Can not find " + saves);
        }
        List<String> playersLoaded =  lobby.loadGame("SaveGameTest", uniqueID.get());
        assertNotEquals(0, playersLoaded.size());
    }

    @Test
    void getLoadedPlayersNames() throws WaitLobbyException, IllegalLobbyException, GameLoadException, GameNameException {
        Optional<String> uniqueID = lobby.join();
        List<String> playersLoaded =  lobby.loadGame("SaveGameTest", uniqueID.get());
        assertEquals(playersLoaded, lobby.getLoadedPlayersNames());
    }


    @Test
    public void registerRemoveServers(){
        ServerCommunication s1 = new ServerCommunicationInstance();
        ServerCommunication s2 = new ServerCommunicationInstance();
        ServerCommunication s3 = new ServerCommunicationInstance();

        //Print 1 server registered
        lobby.registerServer(s1);
        //Print 2 servers registered
        lobby.registerServer(s2);
        //No Print
        lobby.removeServer(s3);
        //Print 1 server registered
        lobby.removeServer(s1);
    }

    @Test
    public void resetTest() throws WaitLobbyException, NicknameException, IllegalLobbyException, GameLoadException, GameNameException, NicknameTakenException, FirstPlayerAbsentException, FullGameException, EmptyLobbyException, InterruptedException {
        String playerId = lobby.join().get();
        List<String> playersLoaded =  lobby.loadGame("SaveGameTest", playerId);
        lobby.joinLoadedGameFirstPlayer(playersLoaded.get(0), playerId);
        playersLoaded.remove(playersLoaded.get(0));
        for(String p : playersLoaded){
            lobby.addPlayer(p);
            System.out.println(p);
        }
        ServerCommunication s1 = new ServerCommunicationInstance();
        ServerCommunication s2 = new ServerCommunicationInstance();
        ServerCommunication s3 = new ServerCommunicationInstance();
        ServerCommunication s4 = new ServerCommunicationInstance();
        lobby.registerServer(s1);
        lobby.registerServer(s2);
        lobby.registerServer(s3);
        lobby.registerServer(s4);
        lobby.startGame();
        //Simulation s1
        Thread t1 = new Thread(() -> lobby.reset());
        //Simulation s2
        Thread t2 = new Thread(() -> lobby.reset());
        //Simulation s3
        Thread t3 = new Thread(() -> lobby.reset());
        //Simulation s4
        Thread t4 = new Thread(() -> lobby.reset());
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        System.out.println("After reset: "+lobby);
        assertFalse(lobby.isPlaying());
        assertFalse(lobby.isGameLoaded());
        assertFalse(lobby.isFistPlayerPresent());
        assertNull(lobby.getNameFromId(playerId));
        assertNull(lobby.getControllerProvider());
    }

    @Test
    public void restCheckSafeReset() throws InterruptedException, NicknameException, WaitLobbyException, IllegalLobbyException, GameLoadException, GameNameException, NicknameTakenException, FirstPlayerAbsentException, FullGameException, EmptyLobbyException {
        String playerId = lobby.join().get();
        List<String> playersLoaded =  lobby.loadGame("SaveGameTest", playerId);
        String player1 = playersLoaded.get(0);
        lobby.joinLoadedGameFirstPlayer(player1, playerId);
        playersLoaded.remove(player1);
        for(String p : playersLoaded){
            lobby.addPlayer(p);
            System.out.println(p);
        }
        class ServerCommunicationInstance implements ServerCommunication{

            /**
             * Send one GameSetUp object to every player
             */
            @Override
            public void gameSetUp() {

            }

            /**
             * If in game, function notifies the disconnection of a player to all the others
             *
             * @param playerName player that disconnect
             */
            @Override
            public void notifyDisconnection(String playerName) {

            }

            /**
             * Notify to all clients that a player reconnected
             *
             * @param playerName name of the player who reconnected
             */
            @Override
            public void notifyReconnection(String playerName) {

            }

            /**
             * Send a message to all players
             *
             * @param sender  sender's name
             * @param date    date of message creation
             * @param message actual message to be sent
             */
            @Override
            public void notifyMessage(String sender, String date, String message) {

            }

            /**
             * Send a message to all players
             *
             * @param sender   sender's name
             * @param date     date of message creation
             * @param message  actual message to be sent
             * @param receiver receiver's name
             */
            @Override
            public void notifyMessage(String sender, String date, String message, String receiver) {

            }

            /**
             * Notify change in the board to all clients in game
             *
             * @param tiles board
             */
            @Override
            public void notifyChangeBoard(List<Tile> tiles) {

            }

            /**
             * Notify to all clients change in player's bookshelf
             *
             * @param playerName player's name
             * @param tiles      bookshelf
             */
            @Override
            public void notifyChangeBookShelf(String playerName, List<Tile> tiles) {

            }

            /**
             * Notify change in point of a player to all clients
             *
             * @param playerName player's name
             * @param points     new points
             */
            @Override
            public void updatePlayerPoints(String playerName, int points) {

            }

            /**
             * Notify to all player whom turn is
             *
             * @param playerName current player
             */
            @Override
            public void notifyTurn(String playerName) {

            }

            /**
             * Notify to all clients a change in common goals cards and tokens
             *
             * @param cardsAndTokens cards with associated tokens
             */
            @Override
            public void sendCommonGoalsCards(Map<String, List<Integer>> cardsAndTokens) {

            }

            /**
             * Send notification of the winner to all players
             *
             * @param playerName name of the winner
             */
            @Override
            public void notifyWinner(String playerName) {

            }

            /**
             * Handle the disconnection of the last player terminating the game
             */
            @Override
            public void handleLastPlayerDisconnection() {

            }
        }
        ServerCommunication s1 = new ServerCommunicationInstance();
        ServerCommunication s2 = new ServerCommunicationInstance();
        ServerCommunication s3 = new ServerCommunicationInstance();
        ServerCommunication s4 = new ServerCommunicationInstance();
        lobby.registerServer(s1);
        lobby.registerServer(s2);
        lobby.registerServer(s3);
        lobby.registerServer(s4);
        lobby.startGame();
        //Simulation s1
        Thread t1 = new Thread(() -> lobby.reset());
        //Simulation s2
        Thread t2 = new Thread(() -> lobby.reset());
        //Simulation s3
        Thread t3 = new Thread(() -> lobby.reset());
        //Simulation s4
        Thread t4 = new Thread(() -> lobby.reset());
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        assertTrue(lobby.isPlaying());
        assertTrue(lobby.isGameLoaded());
        assertTrue(lobby.isFistPlayerPresent());
        assertEquals(player1,lobby.getNameFromId(playerId));
        assertNotNull(lobby.getControllerProvider());
        t4.start();
        t4.join();
        System.out.println("After reset: "+lobby);
        assertFalse(lobby.isPlaying());
        assertFalse(lobby.isGameLoaded());
        assertFalse(lobby.isFistPlayerPresent());
        assertNull(lobby.getNameFromId(playerId));
        assertNull(lobby.getControllerProvider());
    }
}