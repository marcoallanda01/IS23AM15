package it.polimi.ingsw.client;

import it.polimi.ingsw.client.communication.ClientNotificationListener;
import it.polimi.ingsw.communication.responses.GameSetUp;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.*;
import java.util.stream.Collectors;

public class ClientController implements ClientNotificationListener {
    private final View view;

    public ClientController() {
        view = Client.getInstance().getView();
    }

    @Override
    public void notifyGame(GameSetUp gameSetUp) {
        Client.getInstance().setClientState(ClientStates.IN_GAME);
        view.setPlayers(gameSetUp.players);
        view.setCommonGoals(gameSetUp.goals);
        view.render();
    }

    @Override
    public void notifyWinner(String nickname) {
        Client.getInstance().setClientState(ClientStates.END_GAME);
        view.setWinner(nickname);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyBoard(Set<Tile> tiles) {
        view.setLivingRoomBoard(tiles);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyBookshelf(String nickname, Set<Tile> tiles) {
        Map<String, Set<Tile>> bookShelves = view.getBookShelves();
        bookShelves.put(nickname, tiles);
        view.setBookShelves(bookShelves);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyPoints(String nickname, int points) {
        Map<String, Integer> pointsMap = view.getPoints();
        pointsMap.put(nickname, points);
        view.setPoints(pointsMap);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyTurn(String nickname) {
        view.setCurrentTurnPlayer(nickname);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyPersonalGoalCard(String nickname, String card) {
        view.setPersonalGoal(card);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyCommonCards(Map<String, List<Integer>> cardsToTokens) {
        view.setCommonCards(cardsToTokens);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyCommonGoals(Set<String> goals) {
        List<String> goalsList = new ArrayList<>(goals);
        view.setCommonGoals(goalsList);

        if (Client.getInstance().getClientState() == ClientStates.IN_GAME) {
            view.render();
        }
    }

    @Override
    public void notifyChatMessage(String nickname, String message, String date) {
        Map<String, Map<String, String>> chat = view.getChat();
        chat.put(date, Map.of("nickname", nickname, "message", message));
        view.setChat(chat);

        //view.render();
    }

    @Override
    public void notifyDisconnection(String nickname) {
        view.render();
        view.showError("The player " + nickname + " has disconnected.");
    }

    @Override
    public void notifyGameSaved(String game) {
        view.render();
        view.showError("The game has been saved successfully.");
    }

    @Override
    public void notifyPing() {
        Client.getInstance().getClientCommunication().pong(Client.getInstance().getId());
    }

    @Override
    public void notifyReconnection(String nickname) {
        view.render();
        view.showError("The player " + nickname + " has reconnected.");
    }

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

    @Override
    public void notifyLoadedGamePlayers(Set<String> nicknames) {
        List<String> players = new ArrayList<>(nicknames);
        view.setLobbyPlayers(players);
        Client.getInstance().getClientCommunication().joinLoadedAsFirst(view.getNickname(), Client.getInstance().getId());
    }

    @Override
    public void notifyHello(boolean lobbyReady, String firstPlayerId, boolean loadedGame) {
        if (!lobbyReady) {
            if (!firstPlayerId.equals("NoFirst")) {
                Client.getInstance().setId(firstPlayerId);
                Client.getInstance().setClientState(ClientStates.CREATE_LOBBY);
                view.render();
            } else {
                view.render();
                view.showError("The lobby is being created by another player. Please retry later.");
            }
        } else {
            Client.getInstance().getClientCommunication().join(Client.getInstance().getView().getNickname());
//            if (loadedGame) {
//                Client.getInstance().getClientCommunication().getLoadedGamePlayers();
//            } else {
//
//            }
        }
    }

    @Override
    public void notifySavedGames(Set<String> games) {
        List<String> savedGames = new ArrayList<>(games);
        view.setSavedGames(savedGames);

        view.render();
    }

    @Override
    public void notifyJoinResponse(boolean result, String error, String id) {
        if (result) {
            Client.getInstance().setId(id);
        } else {
            view.render();
            view.showError(error);
        }
    }

    @Override
    public void notifyLoadGameResponse(boolean result, String error) {
        if (result) {
            Client.getInstance().getClientCommunication().getLoadedGamePlayers();
        } else {
            view.render();
            view.showError(error);
        }
    }

    @Override
    public void notifyError(String message) {
        view.showError(message);
    }

    @Override
    public void notifyPickedTiles(String nickname, List<TileType> tiles) {

    }

    public void login(String nickname) {
        view.setNickname(nickname);
        Client.getInstance().getClientCommunication().hello();
    }

    public void logout() {
        Client.getInstance().getClientCommunication().disconnect(Client.getInstance().getId());
    }

    public void createLobby(boolean loadGame) {
        if (loadGame) {
            Client.getInstance().setClientState(ClientStates.LOAD_GAME);
            Client.getInstance().getClientCommunication().getSavedGames();
        } else {
            Client.getInstance().setClientState(ClientStates.CREATE_GAME);
            view.render();
        }
    }

    public void createGame(int numOfPlayers, boolean easyRules) {
        view.setNumberOfPlayers(numOfPlayers);
        view.setEasyRules(easyRules);
        Client.getInstance().getClientCommunication().joinNewAsFirst(view.getNickname(), numOfPlayers, Client.getInstance().getId(), easyRules);
    }

    public void getSavedGames() {
        Client.getInstance().getClientCommunication().getSavedGames();
    }

    public void loadGame(int index) {
        Client.getInstance().getClientCommunication().loadGame(view.getSavedGames().get(index), view.getNickname());
    }

    public void pickTiles(List<List<Integer>> coordinates) {
        Set<Tile> livingRoomBoard = view.getLivingRoomBoard();
        Set<Tile> tiles = new HashSet<>();
        for(List<Integer> coordinate : coordinates) {
            int x = coordinate.get(0);
            int y = coordinate.get(1);
            for(Tile tile : livingRoomBoard) {
                if(tile.getX() == x && tile.getY() == y) {
                    tiles.add(tile);
                }
            }
        }
        view.setPickedTiles(tiles);
        Client.getInstance().getClientCommunication().pickTiles(Client.getInstance().getId(), tiles);
    }

    public void putTiles(Integer column) {
        List<TileType> tiles = view.getPickedTiles().stream().map(Tile::getType).collect(Collectors.toList());
        Client.getInstance().getClientCommunication().putTiles(Client.getInstance().getId(), tiles, column);
    }

    public void sendChatMessage(String message) {
        Client.getInstance().getClientCommunication().sendMessage(Client.getInstance().getId(), message);
    }

    public void sendChatMessage(String receiver, String message) {
        Client.getInstance().getClientCommunication().sendMessage(Client.getInstance().getId(), message, receiver);
    }

    public void saveGame() {
        //Client.getInstance().getClientCommunication().saveGame();
    }
}
