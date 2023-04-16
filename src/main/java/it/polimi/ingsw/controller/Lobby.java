package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Lobby {
    private final Set<String> uniquePlayers;
    private String firstPlayer;
    private int numPlayerGame;

    private final String directory;
    private Optional<Game> loadingGame;
    private boolean easyRules;


    public Lobby(String directory) {
        this.directory = directory;
        this.uniquePlayers = new HashSet<String>();
        this.firstPlayer = null;
        this.numPlayerGame = -1;
        this.loadingGame = Optional.empty();
        this.easyRules = false;
    }

    public boolean addFirstPlayer(String player, int numPlayersGame){
        if(this.firstPlayer == null){
            this.firstPlayer = new String(player);
            this.numPlayerGame = numPlayersGame;
            this.uniquePlayers.add(this.firstPlayer);
        }
        return false;
    }

    public void addPlayer(String player) throws GameIsFullException, NicknameException, NicknameTakenException {
        if(this.uniquePlayers.size() == this.numPlayerGame){
            throw new GameIsFullException(player);
        }
        if(this.uniquePlayers.contains(player)){
            throw new NicknameTakenException(player);
        }
        if(this.loadingGame.isPresent()){
            if(!this.loadingGame.get().getPlayers().contains(player)){
                throw new NicknameException(player);
            }
        }
        uniquePlayers.add(player);
    }

    public boolean removePlayer(String player){
        if(this.uniquePlayers.contains(player)){
            this.uniquePlayers.remove(player);
            return true;
        }
        return false;
    }

    public void setEasyRules(boolean yes) {
        this.easyRules = yes;
    }

    public boolean isReadyToPlay(){
        return this.uniquePlayers.size() == this.numPlayerGame;
    }

    public ControllerProvaider startGame(){
        return new ControllerProvaider( new Game(new ArrayList<>(this.uniquePlayers), easyRules) );
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


    /**
     * Load saved game
     * @param name the name of the saved game
     * @return the list of loaded game players' nickname
     */
    public List<String> loadGame(String name) throws GameLoadException, GameNameException {
        if( !getSavedGames().contains(name)) {
            throw new GameNameException(name);
        }
        try{
            // TODO: parsing file
            Game game = new Game(...);

            return new ArrayList<String>(game.getPlayers());
        } catch (Exception e) {
            throw new GameLoadException(name, e);
        }
    }
}
