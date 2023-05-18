package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.communication.ServerCommunication;
import it.polimi.ingsw.server.controller.exceptions.*;
import it.polimi.ingsw.server.controller.serialization.GameTypeAdapter;
import it.polimi.ingsw.server.model.exceptions.ArrestGameException;
import it.polimi.ingsw.server.model.Game;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the lobby of the game. It is used to manage the players that are waiting to play.
 * It is also used to load and save games.
 */
public class Lobby {
    /**
     * Maps the id of the player to the name of the player
     */
    private Map<String, String> players;
    private String firstPlayerId;
    /**
     * The number of players that will play the game
     */
    private int numPlayersGame;
    private final int maxNumPlayers = 4;
    private final int minNumPlayers = 2;
    /**
     * The directory where the games are saved
     */
    private String directory;
    /**
     * The game that is being loaded
     */
    private Game loadingGame;
    /**
     * True if the game is using the easy rules, false otherwise
     */
    private boolean easyRules;
    /**
     * True if the game is in creation, false otherwise
     */
    private boolean isCreating;

    private Set<String> games;

    private Game currentGame;

    /**
     * Attribute needed to understand if the lobby was already reset or not
     */
    private int resets;

    /**
     * attribute to count how many servers are using the lobby
     */
    private int numServers;

    private final PushNotificationController pushNotificationController;

    private ControllerProvider controllerProvider;
    private boolean isPlaying;

    /**
     * Create a lobby
     * @param directory directory where game are saved
     */
    public Lobby(@NotNull String directory) {
        this.pushNotificationController = new PushNotificationController(new ArrayList<>());
        this.numServers = 0;
        this.resets = 0;
        this.currentGame = new Game(this.pushNotificationController);
        this.directory = directory;
        this.firstPlayerId = null;
        this.numPlayersGame = -1;
        this.loadingGame = null;
        this.easyRules = false;
        this.players = new HashMap<>();
        this.isCreating = false;
        this.controllerProvider = null;
        this.isPlaying = false;

        this. games = new HashSet<>();
        File saves = new File(this.directory);
        if (! (!saves.exists() || saves.isFile()) ) {
            // saves for sure is not a file
            File[] savesList = saves.listFiles();
            games = Arrays.stream(savesList != null ? savesList : new File[0]).sequential().
                    filter(File::isFile)
                    .map(File::getName)
                    .map((name) -> (name.substring(0, name.lastIndexOf('.'))))
                    .collect(Collectors.toSet());
        }
    }

    /**
     * Taste join of a player
     * @return an Optional containing a unique id to the first player to connect, else an empty Optional and addPlayer
     * can be called
     * @throws WaitLobbyException if the there is already a first player and the game is in creation
     */
    public synchronized Optional<String> join() throws WaitLobbyException { //HELLO COMMAND
        System.out.println("Before join(hello): "+this);
        Optional<String> uniqueID = Optional.empty();
        if (this.firstPlayerId == null) {
            this.firstPlayerId = UUID.randomUUID().toString();
            uniqueID = Optional.of(this.firstPlayerId);
            this.isCreating = true;
        } else if (this.isCreating) {
            throw new WaitLobbyException();
        }
        return uniqueID;
    }

    /**
     * Get if first player is creating the game
     * @return true if first player is creating the game
     */
    public synchronized boolean getIsCreating(){
        return this.isCreating;
    }

    /**
     * Check if game has been loaded
     * @return true if game created has been loaded from a  save
     */
    public synchronized boolean isGameLoaded(){
        return loadingGame != null;
    }

    /**
     * Get all saves
     * @return a Set of the saved games names
     */
    public synchronized Set<String> getSavedGames() {
        return this.games;
    }

    /**
     * Get the players' names from the loaded game
     * @return list of names
     */
    public synchronized List<String> getLoadedPlayersNames(){
        List<String> players = new ArrayList<>();
        if(loadingGame != null){
            players.addAll(loadingGame.getPlayers());
        }
        return players;
    }


    /**
     * First player join and creates a new game
     * @param name future nickname in the game
     * @param numPlayersGame players in the game. From 2 to 4
     * @param easyRules set true for easy rule game mode
     * @param id first player's id
     * @return true if game creation went well
     */
    public synchronized boolean joinFirstPlayer(String name, int numPlayersGame, boolean easyRules, String id) {
        System.out.println("Before joinFirstPlayer: "+this);
        if (this.isCreating && this.loadingGame == null && numPlayersGame <= this.maxNumPlayers && numPlayersGame >= this.minNumPlayers &&
                this.firstPlayerId != null && this.firstPlayerId.equals(id)) { // this.isCreating can be true only when fistPlayer.isPresent()
            this.isCreating = false;
            this.easyRules = easyRules;
            this.players.put(this.firstPlayerId, name);
            this.numPlayersGame = numPlayersGame;
            return true;
        }
        return false;
    }


    /**
     * Load saved game
     *
     * @param name          the name of the saved game
     * @param idFirstPlayer the id of the first player to connect
     * @return the list of loaded game players nicknames
     */
    public synchronized List<String> loadGame(String name, String idFirstPlayer) throws GameLoadException, GameNameException, IllegalLobbyException {
        System.out.println("Before loadgame: "+this);
        if (this.isCreating && this.firstPlayerId.equals(idFirstPlayer)) {
            if (!getSavedGames().contains(name)) {
                throw new GameNameException(name);
            }
            try {
                Gson gson = new GsonBuilder().registerTypeAdapter(Game.class, new GameTypeAdapter()).create();
                BufferedReader reader = new BufferedReader(new FileReader(this.directory + "/" + name + ".json"));
                Game game = gson.fromJson(reader, Game.class);
                this.loadingGame = game;
                this.numPlayersGame = game.getPlayers().size();
                this.easyRules = game.isFirstGame();
                return new ArrayList<>(game.getPlayers());
            } catch (Exception e) {
                throw new GameLoadException(name, e);
            }
        } else {
            throw new IllegalLobbyException();
        }
    }

    /**
     * First player join a game previously loaded
     * @param name future nickname in the game from those available
     * @param id first player's id
     * @return true if game creation went well
     */
    public synchronized boolean joinLoadedGameFirstPlayer(String name, String id) throws NicknameException {
        System.out.println("Before joinLoadedGameFirstPlayer: "+this);
        if (this.isCreating && this.loadingGame != null && this.firstPlayerId.equals(id)) {
            if (!this.loadingGame.getPlayers().contains(name)) {
                throw new NicknameException(name);
            }
            this.isCreating = false;
            this.players.put(this.firstPlayerId, name);
            return true;
        }
        return false;
    }

    /**
     * Add a player to the lobby
     * @param player player name
     * @return id of the player that joined
     * @throws FullGameException game is full / game is being played
     * @throws NicknameTakenException this name was taken from another player
     * @throws NicknameException if the game is loaded and the player do not take one of the available ones
     * @throws FirstPlayerAbsentException if there is not a first player yet
     */
    public synchronized String addPlayer(@NotNull String player) throws FullGameException, NicknameTakenException, NicknameException, FirstPlayerAbsentException {
        if (this.firstPlayerId == null){
            throw new FirstPlayerAbsentException();
        }
        System.out.println("Before addPlayer: "+this);
        Set<String> uniquePlayers = new HashSet<>(this.players.values());
        System.out.println("addPlayer: before checks");
        if (uniquePlayers.size() == this.numPlayersGame) {
            System.out.println("addPlayer: FullGameException");
            throw new FullGameException(player);
        }
        if (uniquePlayers.contains(player)) {
            System.out.println("addPlayer: NicknameTakenException");
            throw new NicknameTakenException(player);
        }
        if (this.loadingGame != null) {
            if (!this.loadingGame.getPlayers().contains(player)) {
                System.out.println("addPlayer: NicknameException");
                throw new NicknameException(player);
            }
        }
        String id;
        System.out.println("addPlayer: Before generating id");
        do {
            id = UUID.randomUUID().toString();
            System.out.println("addPlayer: Generated id->"+id);
        } while (this.players.putIfAbsent(id, player) != null);

        return id;
    }

    /**
     * Remove a player from the lobby. If first player is removed while them are creating the game than the lobby is
     * reset
     * @param playerId player's id
     * @return true if remove was successful, false if it wasn't or player didn't exist
     */
    public synchronized boolean removePlayer(String playerId) {
        if(Objects.equals(this.firstPlayerId, playerId) && this.isCreating){
            forceReset();
            return true;
        }else {
            return this.players.remove(playerId) != null;
        }
    }

    /**
     * Check if first player is present
     * @return true if first player is present
     */
    public synchronized boolean isFistPlayerPresent() {
        return this.firstPlayerId != null;
    }

    /**
     * Return if the lobby could start
     * @return true if the lobby is full
     */
    private boolean isReadyToPlay() {
        return this.players.size() == this.numPlayersGame;
    }

    /**
     * Start a game
     * @return ControllerProvider to manage the game
     * @throws EmptyLobbyException lobby is not full
     * @throws ArrestGameException something went wrong in the model
     */
    public synchronized ControllerProvider startGame() throws EmptyLobbyException, ArrestGameException {
        System.out.println("Before startGame: " + this);
        if (!isReadyToPlay()) {
            throw new EmptyLobbyException(this.players.size(), this.numPlayersGame);
        }
        if (loadingGame == null) {
            System.out.println("Lobby startGame: starting new game");
            try {
                this.isPlaying = true;
                this.currentGame.setGame(new ArrayList<>(this.players.values()), this.easyRules);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // TODO: brutto così, non si può non usare un costruttore per fare il load?
            System.out.println("Lobby startGame: starting loaded game");
            this.isPlaying = true;
            this.currentGame.setGame(this.loadingGame);
        }
        System.out.println("Lobby startGame: creating controller provider...");
        this.controllerProvider = new ControllerProvider(this.currentGame, this.directory);
        System.out.println("Lobby startGame: before return, controller provider created");
        return this.controllerProvider;
    }

    /**
     * Ask if the game started
     * @return true if game is being played
     */
    public synchronized boolean isPlaying(){
        return this.isPlaying;
    }

    /**
     * get current controller provider
     * @return controller provider | null if not present
     */
    public ControllerProvider getControllerProvider(){
        return this.controllerProvider;
    }


    /**
     * Get player's id from player's name
     * @param name player's name
     * @return player's id . Returns null if there is not such player
     */
    public synchronized String getIdFromName(String name) {
        return this.players.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), name)).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    /**
     * Get player's name from player's id
     * @param id player's id
     * @return player's name . Returns null if there is not such player
     */
    public synchronized String getNameFromId(String id){
        return this.players.get(id);
    }

    /**
     * Force a reset of the lobby also when the game is not ended
     */
    private void forceReset(){
        this.firstPlayerId = null;
        this.numPlayersGame = -1;
        this.loadingGame = null;
        this.easyRules = false;
        this.players = new HashMap<>();
        this.isCreating = false;
        this.controllerProvider = null;
        this.isPlaying = false;

        this.games = new HashSet<>();
        File saves = new File(this.directory);
        if (!(!saves.exists() || saves.isFile())) {
            // saves for sure is not a file
            File[] savesList = saves.listFiles();
            games = Arrays.stream(savesList != null ? savesList : new File[0]).sequential().
                    filter(File::isFile)
                    .map(File::getName)
                    .map((name) -> (name.substring(0, name.lastIndexOf('.'))))
                    .collect(Collectors.toSet());
        }
        this.currentGame = new Game(pushNotificationController);
    }

    /**
     * Reset game and lobby.
     * Note MUST CALL when game is ended
     */
    public synchronized void reset(){
        this.resets++;
        if(this.resets == this.numServers) {
            this.directory = directory;
            this.firstPlayerId = null;
            this.numPlayersGame = -1;
            this.loadingGame = null;
            this.easyRules = false;
            this.players = new HashMap<>();
            this.isCreating = false;
            this.controllerProvider = null;
            this.isPlaying = false;

            this.games = new HashSet<>();
            File saves = new File(this.directory);
            if (!(!saves.exists() || saves.isFile())) {
                // saves for sure is not a file
                File[] savesList = saves.listFiles();
                games = Arrays.stream(savesList != null ? savesList : new File[0]).sequential().
                        filter(File::isFile)
                        .map(File::getName)
                        .map((name) -> (name.substring(0, name.lastIndexOf('.'))))
                        .collect(Collectors.toSet());
            }
            this.currentGame = new Game(pushNotificationController);

            this.resets = 0;
        }
    }

    /**
     * Register a server to play and receive notifications about model status
     * @param server server
     */
    public synchronized void registerServer(ServerCommunication server){
        if(this.pushNotificationController.addServer(server)){
            this.numServers++;
            System.out.println("Lobby: server added. Now there are "+this.numServers+" servers");
        }
    }

    /**
     * Remove server's privilege to play and receive notifications about model status
     * @param server server
     */
    public synchronized void removeServer(ServerCommunication server){
        if(this.pushNotificationController.removeServer(server)) {
            this.numServers--;
            System.out.println("Lobby: server removed. Now there are " + this.numServers + " servers");
        }
    }

    @Override
    public String toString() {
        String pink = "\u001B[35m";  // ANSI escape code for pink color
        String reset = "\u001B[0m";  // ANSI escape code to reset color
        return pink+"Lobby{" +
                "players=" + players +
                ", firstPlayerId='" + firstPlayerId + '\'' +
                ", numPlayersGame=" + numPlayersGame +
                ", directory='" + directory + '\'' +
                ", loadingGame=" + loadingGame +
                ", easyRules=" + easyRules +
                ", isCreating=" + isCreating +
                ", games=" + games +
                ", currentGame=" + currentGame +
                ", resets=" + resets +
                ", numServers=" + numServers +
                ", controllerProvider=" + controllerProvider +
                '}'+reset;
    }
}
