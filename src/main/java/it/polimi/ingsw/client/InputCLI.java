package it.polimi.ingsw.client;

import java.util.Scanner;

public class InputCLI {
    private static boolean global;

    public static void inputHandler(Scanner inputScanner) {
        while(inputScanner.hasNext()){
            String input = inputScanner.next();
            input = input.trim();
            String[] inputArray = input.split(" ");
            if(inputArray[0].equalsIgnoreCase("refresh")){
                Client.getInstance().getView().render();
                return;
            }

            switch (Client.getInstance().getClientState()){
                case LOGIN:
                    loginRequest(inputArray);
                    break;
                case CREATE_LOBBY:
                    createLobbyRequest(inputArray);
                    break;
                case LOBBY:
                    switch (inputArray[0].toLowerCase()){
                        case "join":
                            joinLobbyRequest(inputArray);
                            break;
                        case "start":
                            startGameRequest(inputArray);
                            break;
                        default:
                            System.out.println("Invalid input");
                            break;
                    }
                    break;
                case IN_GAME:
                    switch (inputArray[0].toLowerCase()){
                        case "pick":
                            pickTilesRequest(inputArray);
                            break;
                        case "put":
                            putTilesRequest(inputArray);
                            break;
                        case "chat":
                            sendChatMessageRequest(inputArray);
                            break;
                        case "show":
                            showGoalCardsRequest(inputArray);
                            break;
                        default:
                            System.out.println("Invalid input");
                            break;
                    }
                    break;
                case END_GAME:
                    showWinnerRequest(inputArray);
                    break;
            }
        }
    }

    private static void loginRequest(String[] inputArray){
        if(inputArray.length != 1){
            System.out.println("Invalid input");
            return;
        }
        ClientController.login(inputArray[0]);
    }

    private static void logoutRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        ClientController.logout();
    }

    private static void createLobbyRequest(String[] inputArray) {
        if (inputArray.length < 1 || inputArray.length> 2) {
            System.out.println("Invalid input");
            return;
        }
        int numPlayers;
        try {
            numPlayers = Integer.parseInt(inputArray[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            return;
        }
        if (inputArray.length == 2) {
            if (inputArray[1].equalsIgnoreCase("easyRules")) {
                ClientController.createLobby(numPlayers, true);
            } else{
                System.out.println("Invalid input");
            }
        } else {
            ClientController.createLobby(numPlayers);
        }
    }

    private static void joinLobbyRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        ClientController.joinLobby();
    }

    private static void startGameRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        ClientController.startGame();
    }

    private static void pickTilesRequest(String[] inputArray) {
        if (inputArray.length <2 || inputArray.length > 4) {
            System.out.println("Invalid input");
            return;
        }

    }

    private static void putTilesRequest(String[] inputArray) {
        if (inputArray.length != 5) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send put tiles request
    }

    private static void sendChatMessageRequest(String[] inputArray) {
        if (inputArray.length < 2) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send send chat message request
    }

    private static void showGoalCardsRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send show goal cards request
    }

    private static void showWinnerRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send show winner request
    }
}
