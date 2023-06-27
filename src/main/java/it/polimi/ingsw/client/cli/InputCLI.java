package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientStates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * InputCLI class is the class that handles the input from the user
 */
public class InputCLI {
    /**
     * Default constructor
     */
    public InputCLI() {
    }

    /**
     * Handles the input from the user based on the current state of the client
     *
     * @param inputScanner the scanner that reads the input
     */
    public static void inputHandler(Scanner inputScanner) {
        while (inputScanner.hasNext()) {
            String input = inputScanner.next();
            input = input.trim();
            String[] inputArray = input.split(" ");
            if (inputArray[0].equalsIgnoreCase("refresh")) {
                Client.getInstance().getView().render();
                return;
            }

            boolean globalCommand = globalCommand(inputArray);

            if (!globalCommand) {
                switch (Client.getInstance().getClientState()) {
                    case STARTUP -> {
                        Runtime.getRuntime().exit(0);
                        return;
                    }
                    case LOGIN -> loginRequest(inputArray);
                    case CREATE_LOBBY -> createLobbyRequest(inputArray);
                    case CREATE_GAME -> createGameRequest(inputArray);
                    case LOAD_GAME -> loadGameRequest(inputArray);
                    case LOAD_NAMES -> loadNamesRequest(inputArray);
                    case IN_GAME -> {
                        switch (inputArray[0].toLowerCase()) {
                            case "help":
                                helpRequest(inputArray);
                                break;
                            case "pick":
                                pickTilesRequest(inputArray);
                                break;
                            case "put":
                                putTilesRequest(inputArray);
                                break;
                            case "sendmessage":
                                sendChatMessageRequest(inputArray);
                                break;
                            case "showchat":
                                showChatRequest(inputArray);
                                break;
                            case "showgoals":
                                showGoalsRequest(inputArray);
                                break;
                            case "showgame":
                                showGameRequest(inputArray);
                                break;
                            case "save":
                                saveGameRequest(inputArray);
                            default:
                                Client.getInstance().getView().showError("Invalid input");
                                break;
                        }
                    }
                    case END_GAME -> {
                        Runtime.getRuntime().exit(0);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Handles the choice of the nickname for a saved game
     *
     * @param inputArray the input from the user (the nickname)
     */
    private static void loadNamesRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().loginLoaded(inputArray[0]);
    }

    /**
     * Handles the request to show the goals (common and personal)
     *
     * @param inputArray the input from the user
     */
    private static void showGoalsRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getView().showGoals();
    }

    /**
     * Handles the request to show the help menu
     *
     * @param inputArray the input from the user
     */
    private static void helpRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getView().showHelp();
    }

    /**
     * Handles the request to show the current game
     *
     * @param inputArray the input from the user
     */
    private static void showGameRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getView().render();
    }

    /**
     * Handles the request to show the chat window
     *
     * @param inputArray the input from the user
     */
    private static void showChatRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getView().showChat();
    }

    /**
     * Handles the request to log in with a nickname
     *
     * @param inputArray the input from the user (the nickname)
     */
    private static void loginRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().login(inputArray[0]);
    }

    /**
     * Handles the request to log out
     *
     * @param inputArray the input from the user
     */
    private static void logoutRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().logout();
    }

    /**
     * Handles the request to create a lobby
     *
     * @param inputArray the input from the user
     */
    private static void createLobbyRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        if (inputArray[0].equals("1")) {
            Client.getInstance().getClientController().createLobby(false);
        } else if (inputArray[0].equals("2")) {
            Client.getInstance().getClientController().createLobby(true);
        } else {
            Client.getInstance().getView().showError("Invalid input");
        }
    }

    /**
     * Handles the request to load a game
     *
     * @param inputArray the input from the user (the index of the game)
     */
    private static void loadGameRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        int choice;
        try {
            choice = Integer.parseInt(inputArray[0]);
        } catch (NumberFormatException e) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        if (choice < 0 || Client.getInstance().getView().getSavedGames().size() - 1 < choice) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }

        Client.getInstance().getClientController().loadGame(choice);
    }

    /**
     * Handles the request to save a game
     *
     * @param inputArray the input from the user
     */
    private static void saveGameRequest(String[] inputArray) {
        if (inputArray.length != 2) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().saveGame(inputArray[1]);
    }

    /**
     * Handles the request to create a game
     *
     * @param inputArray the input from the user
     */
    private static void createGameRequest(String[] inputArray) {
        if (inputArray.length < 3 || inputArray.length > 4) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        String nickname;
        try {
            nickname = inputArray[0];
        } catch (NumberFormatException e) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        int numPlayers;
        try {
            numPlayers = Integer.parseInt(inputArray[1]);
        } catch (NumberFormatException e) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        if (inputArray.length == 3) {
            if (inputArray[2].equalsIgnoreCase("y")) {
                Client.getInstance().getClientController().createGame(nickname, numPlayers, true);
            } else if ((inputArray[2].equalsIgnoreCase("n"))) {
                Client.getInstance().getClientController().createGame(nickname, numPlayers, false);
            }
        } else {
            Client.getInstance().getView().showError("Invalid input");
        }
    }

    /**
     * Handles the request to pick a list of tiles
     *
     * @param inputArray the input from the user (the coordinates of the tiles)
     */
    private static void pickTilesRequest(String[] inputArray) {
        if (inputArray.length < 2 || inputArray.length > 4) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        List<List<Integer>> coordTiles = new ArrayList<>();
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i].length() != 3 || inputArray[i].charAt(1) != ',') {
                Client.getInstance().getView().showError("Invalid input");
                return;
            }
            List<Integer> coord = new ArrayList<>();
            try {
                coord.add(Integer.parseInt(String.valueOf(inputArray[i].charAt(0))));
                coord.add(Integer.parseInt(String.valueOf(inputArray[i].charAt(2))));
                coordTiles.add(coord);
            } catch (NumberFormatException e) {
                Client.getInstance().getView().showError("Invalid input");
                return;
            }
        }
        Client.getInstance().getClientController().pickTiles(coordTiles);
    }

    /**
     * Handles the request to put a list of tiles
     *
     * @param inputArray the input from the user (the column and the order of the tiles)
     */
    private static void putTilesRequest(String[] inputArray) {
        if (inputArray.length < 3 || inputArray.length != Client.getInstance().getView().getPickedTiles().size() + 2) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        int column;
        List<Integer> order = new ArrayList<>();
        try {
            column = Integer.parseInt(inputArray[1]);
            for (int i = 2; i < inputArray.length; i++) {
                order.add(Integer.parseInt(inputArray[i]));
            }
        } catch (NumberFormatException e) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        //if order contains duplicates or numbers out of range
        if (order.size() != new HashSet<>(order).size() || order.stream().anyMatch(i -> i < 0 || i > Client.getInstance().getView().getPickedTiles().size() - 1)) {
            Client.getInstance().getView().showError("Tiles order is invalid");
            return;
        }
        Client.getInstance().getClientController().putTiles(column, order);
    }

    /**
     * Handles the request to send a chat message
     *
     * @param inputArray the input from the user (the receiver/s and the message)
     */
    private static void sendChatMessageRequest(String[] inputArray) {
        if (inputArray.length < 3) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 2; i < inputArray.length; i++) {
            message.append(inputArray[i]).append(" ");
        }
        if (inputArray[1].equalsIgnoreCase("all")) {
            Client.getInstance().getClientController().sendChatMessage(message.toString());
        } else if (Client.getInstance().getView().getPlayers().contains(inputArray[1])) {
            Client.getInstance().getClientController().sendChatMessage(inputArray[1], message.toString());
        } else {
            Client.getInstance().getView().showError("Invalid input");
        }
    }

    /**
     * Handles a global command (state independent)
     *
     * @param command the command to handle
     */
    private static boolean globalCommand(String[] command) {
        if (command[0].equalsIgnoreCase("logout")) {
            if (Client.getInstance().getClientState() == ClientStates.LOGIN) {
                Client.getInstance().getView().showError("Invalid client state");
                return true;
            }
            logoutRequest(command);
            return true;
        }
        return false;
    }
}
