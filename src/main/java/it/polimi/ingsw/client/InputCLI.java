package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputCLI {
    private static boolean globalCommand;

    public static void inputHandler(Scanner inputScanner) {
        while (inputScanner.hasNext()) {
            String input = inputScanner.next();
            input = input.trim();
            String[] inputArray = input.split(" ");
            if (inputArray[0].equalsIgnoreCase("refresh")) {
                Client.getInstance().getView().render();
                return;
            }

            globalCommand = globalCommand(inputArray);

            if (!globalCommand) {
                switch (Client.getInstance().getClientState()) {
                    case LOGIN:
                        loginRequest(inputArray);
                        break;
                    case CREATE_LOBBY:
                        createLobbyRequest(inputArray);
                        break;
                    case CREATE_GAME:
                        createGameRequest(inputArray);
                        break;
                    case LOAD_GAME:
                        loadGameRequest(inputArray);
                        break;
                    case IN_GAME:
                        switch (inputArray[0].toLowerCase()) {
                            case "pick":
                                pickTilesRequest(inputArray);
                                break;
                            case "put":
                                putTilesRequest(inputArray);
                                break;
                            case "chat":
                                sendChatMessageRequest(inputArray);
                                break;
                            case "save":
                                saveGameRequest(inputArray);
                            default:
                                System.out.println("Invalid input");
                                break;
                        }
                        break;
                }
            }
        }
    }

    private static void loginRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().login(inputArray[0]);
    }

    private static void logoutRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().logout();
    }

    private static void createLobbyRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        if (inputArray[0].equals("1")) {
            Client.getInstance().getClientController().createLobby(true);
        } else if (inputArray[0].equals("2")) {
            Client.getInstance().getClientController().createLobby(false);
        } else {
            Client.getInstance().getView().showError("Invalid input");
        }
    }

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

    private static void saveGameRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        //Client.getInstance().getClientController().saveGame(inputArray[0]);
    }

    private static void createGameRequest(String[] inputArray) {
        if (inputArray.length < 1 || inputArray.length > 2) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        int numPlayers;
        try {
            numPlayers = Integer.parseInt(inputArray[0]);
        } catch (NumberFormatException e) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        if (inputArray.length == 2) {
            if (inputArray[1].equalsIgnoreCase("easyRules")) {
                Client.getInstance().getClientController().createGame(numPlayers, true);
            } else {
                Client.getInstance().getView().showError("Invalid input");
            }
        } else {
            Client.getInstance().getClientController().createGame(numPlayers, false);
        }
    }

    /*private static void joinLobbyRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().joinLobby();
    }*/

    /*private static void startGameRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().startGame();
    }*/

    private static void pickTilesRequest(String[] inputArray) {
        if (inputArray.length < 2 || inputArray.length > 4) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        List<List<Integer>> coordTiles = new ArrayList<>();
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i].length() != 5 || inputArray[i].charAt(0) != '(' || inputArray[i].charAt(4) != ')' || inputArray[i].charAt(2) != ',') {
                Client.getInstance().getView().showError("Invalid input");
                return;
            }
            List<Integer> coord = new ArrayList<>();
            try {
                coord.add(Integer.parseInt(inputArray[i].charAt(1) + ""));
                coord.add(Integer.parseInt(inputArray[i].charAt(3) + ""));
                coordTiles.add(coord);
            } catch (NumberFormatException e) {
                Client.getInstance().getView().showError("Invalid input");
                return;
            }
        }
        Client.getInstance().getClientController().pickTiles(coordTiles);
    }

    private static void putTilesRequest(String[] inputArray) {
        if (inputArray.length != 2) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        int column;
        try {
            column = Integer.parseInt(inputArray[1]);
        } catch (NumberFormatException e) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        Client.getInstance().getClientController().putTiles(column);
    }

    private static void sendChatMessageRequest(String[] inputArray) {
        if (inputArray.length < 3) {
            Client.getInstance().getView().showError("Invalid input");
            return;
        }
        String message = "";
        for (int i = 2; i < inputArray.length; i++) {
            message += inputArray[i] + " ";
        }
        if (inputArray[1].equalsIgnoreCase("all")) {
            Client.getInstance().getClientController().sendChatMessage(message);
        } else if (Client.getInstance().getView().getPlayers().contains(inputArray[1])) {
            Client.getInstance().getClientController().sendChatMessage(inputArray[1], message);
        } else {
            Client.getInstance().getView().showError("Invalid input");
        }
    }

    private static boolean globalCommand(String[] command) {
        switch (command[0].toLowerCase()) {
            case "logout":
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
