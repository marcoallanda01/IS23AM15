package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Game;

import java.io.BufferedReader;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Lobby {
    private final Map<String, String> players; // Map Id to Name
    private String firstPlayerId;
    private int numPlayersGame;
    private final int maxNumPlayers = 4;
    private final int minNumPlayers = 2;
    private final String directory;
    private Game loadingGame;
    private boolean easyRules;
    private boolean isCreating;


    public Lobby(String directory) {
        this.directory = directory;
        this.firstPlayerId = null;
        this.numPlayersGame = -1;
        this.loadingGame = null;
        this.easyRules = false;
        this.players = new HashMap<String, String>();
        this.isCreating = false;
    }

    /**
     * @return an Optional containing a unique id to the first player to connect, else an empty Optional and addPlayer
     *          can be called
     * @throws WaitLobbyException if the there is already a first player and the game is in creation
     */
    public Optional<String> join() throws WaitLobbyException {
        Optional<String> uniqueID = Optional.empty();
        if(this.firstPlayerId.isEmpty()) {
            this.firstPlayerId = UUID.randomUUID().toString();
            this.isCreating = true;
        }
        else if (this.isCreating){
           throw new WaitLobbyException();
        }
        return uniqueID;
    }

    public Set<String> getSavedGames(){
        Set<String> games = new HashSet<String>();
        File saves = new File(this.directory);
        if (!saves.exists() || saves.isFile()) {
            return games;
        }
        // saves for sure is not a file
        File[] savesList = saves.listFiles();
        games = Arrays.stream(savesList).sequential()
                .filter(File::isFile)
                .map(File::getName)                                                       //    9 - 5 - 1  = 3
                .map((name) -> (name.substring(0, name.length() - ".json".length() - 1))) // "ciao.json" -> "ciao"
                .collect(Collectors.toSet());
        return games;
    }


    public boolean joinFirstPlayer(String name, int numPlayersGame, boolean easyRules, String id){
        if(this.isCreating && this.loadingGame != null
                && numPlayersGame <= this.maxNumPlayers && numPlayersGame >= this.minNumPlayers
                && this.firstPlayerId.equals(id)){ // this.isCreating can be true only when fistPlayer.isPresent()
            this.isCreating = false;
            this.easyRules = easyRules;
            this.players.put(this.firstPlayerId, name);
            this.numPlayersGame = numPlayersGame;
        }
        return false;
    }


    /**
     * Load saved game
     * @param name the name of the saved game
     * @return the list of loaded game players' nickname
     */
    public List<String> loadGame(String name, String idFirstPlayer) throws GameLoadException, GameNameException, IllegalLobbyException {
        if (this.isCreating && this.firstPlayerId.equals(idFirstPlayer)) {
            if (!getSavedGames().contains(name)) {
                throw new GameNameException(name);
            }
            try {
                Gson gson = new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter())
                        .registerTypeAdapter(Game.class, new GameTypeAdapter()).create();
                BufferedReader reader = new BufferedReader(new java.io.FileReader(this.directory + "/" + name + ".json"));
                Game game = gson.fromJson(reader, Game.class);
                this.loadingGame = game;
                return new ArrayList<String>(game.getPlayers());
            } catch (Exception e) {
                throw new GameLoadException(name, e);
            }
        } else {
            throw new IllegalLobbyException();
        }
    }

    public boolean joinLoadedGameFirstPlayer(String name, String id) throws NicknameException {
        if(this.isCreating && this.loadingGame != null && this.firstPlayerId.equals(id)) {
            if(!this.loadingGame.getPlayers().contains(name)){
                throw new NicknameException(name);
            }
            this.isCreating = false;
            this.players.put(this.firstPlayerId, name);
            return true;
        }
        return false;
    }

    public String addPlayer(String player) throws FullGameException, NicknameTakenException, NicknameException {
        Set<String> uniquePlayers = (Set<String>) this.players.values();
        if(uniquePlayers.size() == this.numPlayersGame){
            throw new FullGameException(player);
        }
        if(uniquePlayers.contains(player)){
            throw new NicknameTakenException(player);
        }
        if(this.loadingGame != null){
            if(!this.loadingGame.getPlayers().contains(player)){
                throw new NicknameException(player);
            }
        }
        String id;
        do {
            id = UUID.randomUUID().toString();
        }while ( this.players.putIfAbsent(id, player) != null);

        return id;
    }


    private String getIdFromName(String name) {
        return this.players.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), name))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public boolean removePlayer(String player){
        return this.players.remove(getIdFromName(player), player);
    }

    public boolean isFistPlayerPresent(){ return this.firstPlayerId != null; }

    public boolean isReadyToPlay(){
        return this.players.size() == this.numPlayersGame;
    }

    public ControllerProvider startGame() throws EmptyLobbyException{
        if(! isReadyToPlay()){
            throw new EmptyLobbyException(this.players.size(), this.numPlayersGame);
        }
        return new ControllerProvider( new Game(new ArrayList<>(this.players.values()), this.easyRules) );
    }

}
