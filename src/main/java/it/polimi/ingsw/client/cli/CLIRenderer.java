package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientGoal;
import it.polimi.ingsw.client.ClientGoalDetail;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CLI based renderer
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
/**
 * CLIRenderer class is the class that handles the CLI rendering
 * It contains the methods to print the game, the board, the market, the leader cards, the dev cards, the players, the winner, the error, the chat, the help, the goals and the chat notification
 */
public class CLIRenderer {

    /**
     * Prints the game logo
     */
    public synchronized static void printGame() {
        String gameScreen = """
                                                                                                                                                                                               \s
                                                                                                                                                                                               \s
                MMMMMMMM               MMMMMMMM                                 SSSSSSSSSSSSSSS hhhhhhh                                lllllll    ffffffffffffffff    iiii                     \s
                M:::::::M             M:::::::M                               SS:::::::::::::::Sh:::::h                                l:::::l   f::::::::::::::::f  i::::i                    \s
                M::::::::M           M::::::::M                              S:::::SSSSSS::::::Sh:::::h                                l:::::l  f::::::::::::::::::f  iiii                     \s
                M:::::::::M         M:::::::::M                              S:::::S     SSSSSSSh:::::h                                l:::::l  f::::::fffffff:::::f                           \s
                M::::::::::M       M::::::::::Myyyyyyy           yyyyyyy     S:::::S             h::::h hhhhh           eeeeeeeeeeee    l::::l  f:::::f       ffffffiiiiiii     eeeeeeeeeeee   \s
                M:::::::::::M     M:::::::::::M y:::::y         y:::::y      S:::::S             h::::hh:::::hhh      ee::::::::::::ee  l::::l  f:::::f             i:::::i   ee::::::::::::ee \s
                M:::::::M::::M   M::::M:::::::M  y:::::y       y:::::y        S::::SSSS          h::::::::::::::hh   e::::::eeeee:::::eel::::l f:::::::ffffff        i::::i  e::::::eeeee:::::ee
                M::::::M M::::M M::::M M::::::M   y:::::y     y:::::y          SS::::::SSSSS     h:::::::hhh::::::h e::::::e     e:::::el::::l f::::::::::::f        i::::i e::::::e     e:::::e
                M::::::M  M::::M::::M  M::::::M    y:::::y   y:::::y             SSS::::::::SS   h::::::h   h::::::he:::::::eeeee::::::el::::l f::::::::::::f        i::::i e:::::::eeeee::::::e
                M::::::M   M:::::::M   M::::::M     y:::::y y:::::y                 SSSSSS::::S  h:::::h     h:::::he:::::::::::::::::e l::::l f:::::::ffffff        i::::i e:::::::::::::::::e\s
                M::::::M    M:::::M    M::::::M      y:::::y:::::y                       S:::::S h:::::h     h:::::he::::::eeeeeeeeeee  l::::l  f:::::f              i::::i e::::::eeeeeeeeeee \s
                M::::::M     MMMMM     M::::::M       y:::::::::y                        S:::::S h:::::h     h:::::he:::::::e           l::::l  f:::::f              i::::i e:::::::e          \s
                M::::::M               M::::::M        y:::::::y             SSSSSSS     S:::::S h:::::h     h:::::he::::::::e         l::::::lf:::::::f            i::::::ie::::::::e         \s
                M::::::M               M::::::M         y:::::y              S::::::SSSSSS:::::S h:::::h     h:::::h e::::::::eeeeeeee l::::::lf:::::::f            i::::::i e::::::::eeeeeeee \s
                M::::::M               M::::::M        y:::::y               S:::::::::::::::SS  h:::::h     h:::::h  ee:::::::::::::e l::::::lf:::::::f            i::::::i  ee:::::::::::::e \s
                MMMMMMMM               MMMMMMMM       y:::::y                 SSSSSSSSSSSSSSS    hhhhhhh     hhhhhhh    eeeeeeeeeeeeee llllllllfffffffff            iiiiiiii    eeeeeeeeeeeeee \s
                                                     y:::::y                                                                                                                                   \s
                                                    y:::::y                                                                                                                                    \s
                                                   y:::::y                                                                                                                                     \s
                                                  y:::::y                                                                                                                                      \s
                                                 yyyyyyy                                                                                                                                       \s
                                                                                                                                                                                               \s
                                                                                                                                                                                               \s
                """;
        System.out.println(gameScreen);
    }

    /**
     * Prints the request to insert the nickname
     */
    public synchronized static void printLogin() {
        System.out.println("Insert a nickname to join a lobby");
    }

    /**
     * Prints the request to choice to create a new game or load a saved one
     */
    public synchronized static void printCreateLobby() {
        System.out.println("There is no lobby available, would you like to create a new game or load one from the saves?");
        System.out.println("Select an option:");
        System.out.println("1. Create a new game");
        System.out.println("2. Load a saved game");
    }

    /**
     * Prints the request to insert the number of players and the easy rules
     */
    public synchronized static void printCreateGame() {
        System.out.println("To create a new game please specify:");
        System.out.println("- Your nickname");
        System.out.println("- The number of players (2-4)");
        System.out.println("- Should the game use easy rules? (y/n)");
        System.out.println("Please enter the \"(name) (n. of players) (y/n)\"");
    }

    /**
     * Prints saved games
     * @param savedGames the list of saved games
     */
    public synchronized static void printSavedGames(List<String> savedGames) {
        System.out.println("Here's the list of saved games:");
        System.out.println("Select an option:");
        List<String> savedGamesList = new ArrayList<>(savedGames);
        for (String savedGame : savedGamesList) {
            System.out.println(savedGamesList.indexOf(savedGame) + ". " + savedGame);
        }
    }

    /**
     * Prints the lobby screen
     */
    public synchronized static void printLobby() {
        System.out.println("Waiting in lobby for other players to join...");
    }

    /**
     * Prints a Living Room board
     * @param board the board to print
     */
    public synchronized static void printLivingRoomBoard(Set<Tile> board) {
        System.out.println(" ");
        System.out.println("◻️0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣");
        for (int n = 0; n < 9; n++) {
            printLivingRoomBoardLine(board, n);
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints a line of a Living Room board
     * @param board the board to print
     * @param n the number of the line
     */
    public synchronized static void printLivingRoomBoardLine(Set<Tile> board, int n) {
        List<Tile> line = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            line.add(null);
        }
        for (Tile tile : board) {
            if (tile != null && tile.getX() == n) {
                line.set(tile.getY(), tile);
            }
        }
        System.out.print(n + "\uFE0F⃣");
        for (Tile tile : line) {
            if (tile != null) {
                System.out.print(tile.getType().getSymbol());
            } else {
                System.out.print("\uD83D\uDD33");
            }
        }
    }

    /**
     * Prints a bookshelf
     * @param players the list of players (the order of the players in the list is the order of the bookshelves)
     * @param bookshelves the map of bookshelves
     * @param myName the nickname of the client
     * @param currentPlayer the nickname of the current player
     */
    public synchronized static void printBookshelves(List<String> players, Map<String, Set<Tile>> bookshelves, String myName, String currentPlayer) {
        System.out.println();
        for (int n = 5; n >= 0; n--) {
            for (String name : players) {
                Set<Tile> tiles = bookshelves.get(name);
                printBookshelfLine(tiles, n);
                System.out.print("      ");
            }
            System.out.println();
        }

        for (int i = 0; i < bookshelves.values().size(); i++) {
            System.out.print("0️⃣1️⃣2️⃣3️⃣4️⃣      ");
        }
        System.out.println();

        System.out.print("  ");
        for (String name : players) {
            name = name.substring(0, Math.min(name.length(), 8));
            if (name.equals(currentPlayer)) {
                if (name.equals(myName)) {
                    System.out.print(CliColor.RED_UNDERLINED + name + CliColor.RESET);
                } else {
                    System.out.print(CliColor.RED_BOLD_BRIGHT + name + CliColor.RESET);
                }
            } else {
                if (name.equals(myName)) {
                    System.out.print(CliColor.WHITE_UNDERLINED_BRIGHT + name + CliColor.RESET);
                } else {
                    System.out.print(CliColor.WHITE_BRIGHT + name + CliColor.RESET);
                }
            }
            for (int i = 0; i < 18 - name.length(); i++) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * Prints a line of a bookshelf
     * @param tiles the tiles to print
     * @param n the number of the line
     */
    public synchronized static void printBookshelfLine(Set<Tile> tiles, int n) {
        List<Tile> line = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            line.add(null);
        }
        for (Tile tile : tiles) {
            if (tile != null && tile.getY() == n) {
                line.set(tile.getX(), tile);
            }
        }
        for (Tile tile : line) {
            if (tile != null) {
                System.out.print(tile.getType().getSymbol());
            } else {
                System.out.print("\uD83D\uDD33");
            }
        }
    }

    /**
     * Prints the list of the tiles picked by the player
     * @param tiles the list of the tiles picked by the player
     */
    public synchronized static void printPickedTiles(List<TileType> tiles) {
        if (!tiles.isEmpty()) {
            System.out.println("Picked tiles: ");
            for (int i = 0; i < tiles.size(); i++) {
                System.out.print(i + "\uFE0F⃣ ");
            }
            System.out.println();
            for (TileType tile : tiles) {
                System.out.print(tile.getSymbol() + " ");
            }
            System.out.println();
        }

    }

    /**
     * prints the chat
     * @param chat the chat to print
     */
    public synchronized static void printChat(Map<String, Map<String, String>> chat) {
        if(chat.isEmpty()){
            System.out.println("The chat is empty");
            return;
        }
        LinkedHashMap<String, Map<String, String>> sortedChat = chat.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(key -> LocalDateTime.parse(key, DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        for (String dateTimeString : sortedChat.keySet()) {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String time = dateTime.format(hourFormatter);
            String nickname = chat.get(dateTimeString).get("nickname");
            String message = chat.get(dateTimeString).get("message");
            System.out.println(CliColor.WHITE_BOLD_BRIGHT + time + " " + nickname + ": " + CliColor.RESET + message);
        }
    }

    /**
     * Prints the common cards
     * @param commonGoals the map of the common cards
     */
    public synchronized static void printCommonCards(Map<String, List<Integer>> commonGoals) {
        System.out.println("Common cards:");
        for (String name : commonGoals.keySet()) {
            System.out.print(name + ": ");
            for (Integer goal : commonGoals.get(name)) {
                System.out.print(goal + " ");
            }
            System.out.println();
        }
    }

    /**
     * prints the winner of the game and the final scores
     * @param points the map of the points
     * @param winner the nickname of the winner
     */
    public synchronized static void printEndGame(Map<String, Integer> points, String winner) {
        System.out.println("The game is over!");
        System.out.println("Final scores:");
        System.out.println("──────────────────────────────");
        LinkedHashMap<String, Integer> sortedPoints = points.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        for (String name : sortedPoints.keySet()) {
            if (name.equals(winner)) {
                System.out.println(CliColor.GREEN_BOLD_BRIGHT + name + ": " + sortedPoints.get(name) + " WINNER" + CliColor.RESET);
            } else {
                System.out.println(name + ": " + sortedPoints.get(name));
            }
        }

    }

    /**
     * Prints the error message
     * @param message the error message
     */
    public synchronized static void printError(String message) {
        String description = switch (message) {
            case "NicknameTakenException" -> "Nickname already taken";
            case "NicknameException" -> "Nickname not valid";
            case "EmptyLobbyException" -> "Lobby is not full, wait";
            case "FirstPlayerAbsentException" -> "First player is absent";
            case "FullGameException" -> "Game is full";
            case "GameLoadException" -> "Could not load game";
            case "GameNameException" -> "Game name not valid";
            case "IllegalLobbyException" -> "You don't have the permissions";
            case "SaveException" -> "Save not valid";
            case "WaitLobbyException" -> "Wait lobby not valid";
            default -> message;
        };
        System.out.println(CliColor.RED_BOLD_BRIGHT + description + CliColor.RESET);
    }

    /**
     * Prints the help
     */
    public synchronized static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("You may pick tiles with: " + "pick ROW1,COL1 ROW2,COL2 ..., " + "where ROWi and COLi are positive integers");
        System.out.println("You may put tiles with: " + "put COL INDEX1 INDEX2 ..., " + "where COL is the column of your bookshelf and INDEXi represents the i-th picked tile, the order of the indexes represents the order in which tiles will be put in your bookshelf (bottom to top)");
        System.out.println("You may send message to another player with: " + "sendmessage NICKNAME MESSAGE, " + "if NICKNAME is set to 'all' all players will receive the message");
        System.out.println("You may show the chat using: " + "showchat");
        System.out.println("You may show goals details using: " + "showgoals");
        System.out.println("You may show the current game using: " + "showgame");
        System.out.println("You may save the game using: " + "save SAVE_NAME, " + "where SAVE_NAME is the name of the game save, please note that you cannot override existing saves");
    }

    /**
     * Prints the nicknames' players list of a saved game
     * @param gameNames the list of the nicknames' players
     */
    public synchronized static void printLoadedGameNames(List<String> gameNames) {
        System.out.println("Choose a name from the following list:");
        for (String gameName : gameNames) {
            System.out.println(gameName);
        }
    }

    /**
     * Prints the points of the players
     * @param points the map of the points
     */
    public synchronized static void printPoints(Map<String, Integer> points) {
        System.out.println();
        System.out.println("Points:");
        for (String name : points.keySet()) {
            System.out.println(name + ": " + points.get(name));
        }
    }

    /**
     * Prints the goals of the player
     * @param commonGoals the list of the common goals
     * @param commonCards the list of the common cards
     * @param personalGoal the personal goal
     */
    public synchronized static void printGoals(List<ClientGoalDetail> commonCards, List<ClientGoalDetail> commonGoals, ClientGoalDetail personalGoal){
        System.out.println();
        System.out.println(CliColor.YELLOW_BOLD + "Common cards:" + CliColor.RESET);
        for (ClientGoalDetail goal : commonCards) {
            System.out.println(goal.getName() + ": " + goal.getDescription());
            System.out.println();
        }
        System.out.println(CliColor.YELLOW_BOLD + "Common goals:" + CliColor.RESET);
        for (ClientGoalDetail goal : commonGoals) {
            System.out.println(goal.getName() + ": " + goal.getDescription());
            System.out.println();
        }
        System.out.println(CliColor.YELLOW_BOLD + "Personal card:" + CliColor.RESET);
        System.out.println(personalGoal.getName() + ": " + personalGoal.getDescription());
        System.out.println();
    }
}
