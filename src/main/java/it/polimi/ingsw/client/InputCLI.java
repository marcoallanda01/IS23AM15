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
                        case "leave":
                            leaveLobbyRequest(inputArray);
                            break;
                        case "start":
                            startGameRequest(inputArray);
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
        // TODO: send login request
    }

    private static void logoutRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send logout request
    }

    private static void createLobbyRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send create lobby request
    }

    private static void joinLobbyRequest(String[] inputArray) {
        if (inputArray.length != 2) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send join lobby request
    }

    private static void leaveLobbyRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send leave lobby request
    }

    private static void startGameRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send start game request
    }

    private static void pickTilesRequest(String[] inputArray) {
        if (inputArray.length != 3) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send pick tiles request
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
