package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.communication.ClientNotificationListener;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.io.IOException;
import java.util.*;

public class ClientController implements ClientNotificationListener {
    private final View view;
    public ClientController(String goalsPath) {
        view = Client.getInstance().getView();
        try {
            view.setGoalsToDetail(ClientGoalParser.parseGoalsFromJsonFile(getClass().getResource(goalsPath)));
        } catch (IOException e) {
            Client.getInstance().getLogger().log(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is called when the client receives a game setup message from the server.
     *
     * @param gameSetUp the game setup message
     */
    @Override
    public void notifyGame(GameSetUp gameSetUp) {
        Client.getInstance().setClientState(ClientStates.IN_GAME);
        view.setPlayers(gameSetUp.players);
        view.setCommonGoals(gameSetUp.goals);
        view.setPersonalGoal(gameSetUp.personal);
        view.render();
    }

    /*
        * This method is called when the client receives the winner of the game.
        * @param nickname the nickname of the winner
     */
    @Override
    public void notifyWinner(String nickname) {
        Client.getInstance().resetDisconnectTimer(60);
        Client.getInstance().setClientState(ClientStates.END_GAME);
        view.setWinner(nickname);

        if (Client.getInstance().getClientState() == ClientStates.END_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives the board of the game.
        * @param tiles the tiles of the board
     */
    @Override
    public void notifyBoard(Set<Tile> tiles) {
        view.setLivingRoomBoard(tiles);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives a bookshelf of a player.
        * @param nickname the nickname of the player
        * @param tiles the tiles of the bookshelf
     */
    @Override
    public void notifyBookshelf(String nickname, Set<Tile> tiles) {
        Map<String, Set<Tile>> bookShelves = view.getBookShelves();
        bookShelves.put(nickname, tiles);
        view.setBookShelves(bookShelves);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives the points of a player.
        * @param nickname the nickname of the player
        * @param points the points of the player
     */
    @Override
    public void notifyPoints(String nickname, int points) {
        Map<String, Integer> pointsMap = view.getPoints();
        pointsMap.put(nickname, points);
        view.setPoints(pointsMap);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives the name of the current player.
        * @param nickname the nickname of the current player
     */
    @Override
    public void notifyTurn(String nickname) {
        view.setCurrentTurnPlayer(nickname);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives its personal goal card.
        * @param nickname the nickname of the client
        * @param card the personal goal card of the client
     */
    @Override
    public void notifyPersonalGoalCard(String nickname, String card) {
        view.setPersonalGoal(card);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives the common cards of the game.
        * @param cardsToTokens the common cards of the game
     */
    @Override
    public void notifyCommonCards(Map<String, List<Integer>> cardsToTokens) {
        view.setCommonCards(cardsToTokens);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives the common goals of the game.
        * @param goals the common goals of the game
     */
    @Override
    public void notifyCommonGoals(Set<String> goals) {
        List<String> goalsList = new ArrayList<>(goals);
        view.setCommonGoals(goalsList);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    /*
        * This method is called when the client receives a chat message.
        * @param nickname the nickname of the player who sent the message
        * @param message the message
        * @param date the date of the message
     */
    @Override
    public void notifyChatMessage(String nickname, String message, String date) {
        Map<String, Map<String, String>> chat = view.getChat();
        chat.put(date, Map.of("nickname", nickname, "message", message));
        view.setChat(chat);
        view.render();
        if(!nickname.equals(Client.getInstance().getNickname()) && Client.getInstance().getView() instanceof CLI) {
            view.showError("The player " + nickname + " has sent a message, to open the chat type \"showChat\".");
        }
    }

    /*
        * This method is called when the client receives a disconnect message.
        * @param nickname the nickname of the player who disconnected
     */
    @Override
    public void notifyDisconnection(String nickname) {
        view.render();
        view.showError("The player " + nickname + " has disconnected.");
    }

    /*
        * This method is called when the client receives a save message.
        * @param game the game to save
     */
    @Override
    public void notifyGameSaved(String game) {
        view.render();
        view.showError("The game has been saved successfully.");
    }

    /*
        * This method is called when the client receives a ping message.
     */
    @Override
    public void notifyPing() {
        Client.getInstance().resetDisconnectTimer(10);
        Client.getInstance().getClientCommunication().pong(Client.getInstance().getId());
    }

    /*
        * This method is called when the client receives a reconnection message.
        * @param nickname the nickname of the player who reconnected
     */
    @Override
    public void notifyReconnection(String nickname) {
        view.render();
        view.showError("The player " + nickname + " has reconnected.");
    }

    /*
        * This method is called when the client receives the result of joining as the first player.
        * @param result true if the player has joined successfully, false otherwise
        *
     */
    @Override
    public void notifyFirstJoinResponse(boolean result) {
        if (result) {
            Client.getInstance().setClientState(ClientStates.LOBBY);
            view.render();
        } else {
            view.render();
            view.showError("There was an error while creating the lobby. Please try again.");
        }
    }

    /*
        * This method is called when the client receives the list of players in the loaded game.
        * @param nicknames the nicknames of the players in the loaded game
     */
    @Override
    public void notifyLoadedGamePlayers(Set<String> nicknames) {
        List<String> players = new ArrayList<>(nicknames);
        view.setLobbyPlayers(players);
        view.setNumberOfPlayers(players.size());
        Client.getInstance().setClientState(ClientStates.LOAD_NAMES);
        view.render();
        //Client.getInstance().getClientCommunication().joinLoadedAsFirst(Client.getInstance().getNickname(), Client.getInstance().getId());
    }

    /*
        * This method is called when the client receives the handshake response.
        * @param lobbyReady true if the lobby is ready, false otherwise
        * @param firstPlayerId the id of the first player
        * @param loadedGame true if the game is loaded, false otherwise
     */
    @Override
    public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
        if (!lobbyReady) {
            if (!firstPlayerId.equals("NoFirst")) {
                Client.getInstance().setFirstPlayer(true);
                Client.getInstance().setId(firstPlayerId);
                Client.getInstance().setClientState(ClientStates.CREATE_LOBBY);
                view.render();
            } else {
                view.stop("The lobby is being created by another player. Please retry later.");
            }
        } else {
            Client.getInstance().setFirstPlayer(false);
            if(loadedGame) {
                Client.getInstance().getClientCommunication().getLoadedGamePlayers();
            } else {
                Client.getInstance().setClientState(ClientStates.LOGIN);
                view.render();
            }
        }
    }

    /*
        * This method is called when the client receives the list of saved games.
        * @param games the list of saved games
     */
    @Override
    public void notifySavedGames(Set<String> games) {
        List<String> savedGames = new ArrayList<>(games);
        view.setSavedGames(savedGames);

        view.render();
    }
    /*
        * This method is called when the client receives the result of joining the lobby.
        * @param result true if the player has joined successfully, false otherwise
        * @param error the error message
        * @param id the id of the player
     */
    @Override
    public void notifyJoinResponse(boolean result, String error, String id) {
        if (result) {
            Client.getInstance().setId(id);
            Client.getInstance().setClientState(ClientStates.LOBBY);
            view.render();
        } else {
            view.render();
            view.showError(error);
        }
    }

    /*
        * This method is called when the client receives the result of loading a game.
        * @param result true if the game has been loaded successfully, false otherwise
        * @param error the error message
     */
    @Override
    public void notifyLoadGameResponse(boolean result, String error) {
        if (result) {
            Client.getInstance().getClientCommunication().getLoadedGamePlayers();
        } else {
            view.render();
            view.showError(error);
        }
    }
    /*
        * This method is called when the client receives an error message.
        * @param message the error message
     */
    @Override
    public void notifyError(String message) {
        view.showError(message);
    }

    /*
        * This method is called when the client receives the list of tiles he picked
        * @param nickname the nickname of the player who picked the tiles
        * @param tiles the list of tiles he picked
     */
    @Override
    public void notifyPickedTiles(String nickname, List<TileType> tiles) {
        if (nickname.equals(Client.getInstance().getNickname())) {
            view.setPickedTiles(tiles);
            if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
                view.render();
            }
        }
    }

    /*
        * Sends a message to the server to join a lobby.
     */
    public void login(String nickname) {
        Client.getInstance().setNickname(nickname);
        Client.getInstance().getClientCommunication().join(Client.getInstance().getNickname());
    }

    /*
        * Sends a message to the server to logout.
     */
    public void logout() {
        Client.getInstance().getClientCommunication().disconnect(Client.getInstance().getId());
        Client.getInstance().setClientState(ClientStates.STARTUP);
    }

    public void logout(String message) {
        view.stop(message);
        logout();
    }

    /*
        * Transitions either to the saved game selection or to the lobby creation.
     */
    public void createLobby(boolean loadGame) {
        if (loadGame) {
            Client.getInstance().setClientState(ClientStates.LOAD_GAME);
            Client.getInstance().getClientCommunication().getSavedGames();
        } else {
            Client.getInstance().setClientState(ClientStates.CREATE_GAME);
            view.render();
        }
    }

    /*
        * Sends a message to the server to create a lobby.
        * @param nickname the nickname of the player who creates the lobby
        * @param numOfPlayers the number of players in the lobby
        * @param easyRules true if the easy rules are used, false otherwise
     */
    public void createGame(String nickname, int numOfPlayers, boolean easyRules) {
        Client.getInstance().setNickname(nickname);
        view.setNumberOfPlayers(numOfPlayers);
        view.setEasyRules(easyRules);
        Client.getInstance().getClientCommunication().joinNewAsFirst(Client.getInstance().getNickname(), numOfPlayers, Client.getInstance().getId(), easyRules);
    }

    /*
        * Sends a message to the server to fetch the list of saved games.
     */
    public void getSavedGames() {
        Client.getInstance().getClientCommunication().getSavedGames();
    }

    /*
        * Sends a message to the server to load a game.
        * @param index the index of the game to load
     */
    public void loadGame(int index) {
        Client.getInstance().getClientCommunication().loadGame(view.getSavedGames().get(index), Client.getInstance().getId());
    }

    /*
        * Sends a message to the server to join the game as the selected player.
        * @param nickname the nickname of the player selected
     */
    public void loginLoaded(String nickname) {
        Client.getInstance().setNickname(nickname);
        if(Client.getInstance().isFirstPlayer()) {
            Client.getInstance().getClientCommunication().joinLoadedAsFirst(nickname, Client.getInstance().getId());
        } else {
            Client.getInstance().getClientCommunication().join(nickname);
        }
    }

    /*
        * Sends a message to the server to pick the selected tiles.
        * @param coordinates the coordinates of the tiles to pick
     */
    public void pickTiles(List<List<Integer>> coordinates) {
        Set<Tile> livingRoomBoard = view.getLivingRoomBoard();
        Set<Tile> tiles = new HashSet<>();
        for (List<Integer> coordinate : coordinates) {
            int x = coordinate.get(0);
            int y = coordinate.get(1);
            for (Tile tile : livingRoomBoard) {
                if (tile.getX() == x && tile.getY() == y) {
                    tiles.add(tile);
                }
            }
        }
        if (tiles.size() == 0) {
            view.render();
            view.showError("The tiles you selected are not valid. Please retry.");
            return;
        }
        Client.getInstance().getClientCommunication().pickTiles(Client.getInstance().getId(), tiles);
    }

    /*
        * Sends a message to the server to insert the selected tiles.
        * @param column the column where to insert the tiles
        * @param order the order of the tiles to insert
     */
    public void putTiles(Integer column, List<Integer> order) {
        List<TileType> tiles = view.getPickedTiles();
        List<TileType> orderedTiles = new ArrayList<>();
        for (Integer index : order) {
            orderedTiles.add(tiles.get(index));
        }
        Client.getInstance().getClientCommunication().putTiles(Client.getInstance().getId(), orderedTiles, column);
    }

    /*
        * Sends a message to all the players
        * @param message the message to send
     */
    public void sendChatMessage(String message) {
        Client.getInstance().getClientCommunication().sendMessage(Client.getInstance().getId(), message);
    }

    /*
        * Sends a message to a specific player
        * @param receiver the nickname of the receiver
        * @param message the message to send
     */
    public void sendChatMessage(String receiver, String message) {
        Client.getInstance().getClientCommunication().sendMessage(Client.getInstance().getId(), message, receiver);
    }

    /*
        * Sends a message to the server to save the game.
     */
    public void saveGame(String gameName) {
        Client.getInstance().getClientCommunication().saveGame(Client.getInstance().getId(), gameName);
    }

}
