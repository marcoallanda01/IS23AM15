package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.List;
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
            }
        }
    }

    private static void loginRequest(String[] inputArray){
        if(inputArray.length != 1){
            System.out.println("Invalid input");
            return;
        }
        Client.getInstance().getView().nickname = inputArray[0];
        ClientController.login();
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
        List<List<Integer>> coordTiles = new ArrayList<>();
        for (int i = 1; i < inputArray.length; i++) {
            if(inputArray[i].length() != 5 || inputArray[i].charAt(0) != '(' || inputArray[i].charAt(4) != ')'){
                System.out.println("Invalid input");
                return;
            }
            List<Integer> coord = new ArrayList<>();
            try {
                coord.add(Integer.parseInt(inputArray[i].charAt(1)+""));
                coord.add(Integer.parseInt(inputArray[i].charAt(3)+""));
                coordTiles.add(coord);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                return;
            }
        }
        ClientController.pickTiles(coordTiles);
    }

    private static void putTilesRequest(String[] inputArray) {
        if (inputArray.length != 2) {
            System.out.println("Invalid input");
            return;
        }
        Integer column;
        try {
            column = Integer.parseInt(inputArray[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            return;
        }
        ClientController.putTiles(column);
    }

    private static void sendChatMessageRequest(String[] inputArray) {
        if (inputArray.length < 3) {
            System.out.println("Invalid input");
            return;
        }
        String message = "";
        for (int i = 2; i < inputArray.length; i++) {
            message += inputArray[i] + " ";
        }
        if(inputArray[1].equalsIgnoreCase("all")){
            ClientController.sendChatMessage(message);
        } else if(Client.getInstance().getView().players.contains(inputArray[1])){
            ClientController.sendChatMessage(inputArray[1], message);
        } else {
            System.out.println("Invalid input");
        }
    }

    private static void showGoalCardsRequest(String[] inputArray) {
        if (inputArray.length != 1) {
            System.out.println("Invalid input");
            return;
        }
        // TODO: send show goal cards request
    }
}
