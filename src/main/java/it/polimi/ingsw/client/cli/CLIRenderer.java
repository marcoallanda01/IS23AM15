package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientGoalDetail;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

import java.util.*;
import java.util.stream.Collectors;

public class CLIRenderer {

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

    public synchronized static void printLogin() {
        System.out.println("Insert a nickname to join a lobby");
    }

    public synchronized static void printCreateLobby() {
        System.out.println("There is no lobby available, would you like to create a new game or load one from the saves?");
        System.out.println("Select an option:");
        System.out.println("1. Create a new game");
        System.out.println("2. Load a saved game");
    }

    public synchronized static void printCreateGame() {
        System.out.println("To create a new game please specify:");
        System.out.println("- The number of players (2-4)");
        System.out.println("- Should the game use easy rules? (y/n)");
        System.out.println("Please enter the \"create (n. of players) (y/n)\" command");
    }

    public synchronized static void printSavedGames(List<String> savedGames) {
        System.out.println("Here's the list of saved games:");
        System.out.println("Select an option:");
        List<String> savedGamesList = new ArrayList<>(savedGames);
        for (String savedGame : savedGamesList) {
            System.out.println(savedGamesList.indexOf(savedGame) + ". " + savedGame);
        }
    }

    public synchronized static void printLobby(boolean isEasyRules) {
        System.out.println("Waiting in lobby for other players to join...");
        System.out.println("The game will use " + (isEasyRules ? "easy" : "standard") + " rules");
    }

    public synchronized static void printLivingRoomBoard(Set<Tile> board) {
        System.out.println(" ");
        System.out.println("◻️0️⃣1️⃣2️⃣3️⃣4️⃣5️⃣6️⃣7️⃣8️⃣");
        for (int n = 0; n < 9; n++) {
            printLivingRoomBoardLine(board, n);
            System.out.println();
        }
        System.out.println();
    }

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

    public synchronized static void printBookshelves(List<String> players, Map<String, Set<Tile>> bookshelves, String myName, String currentPlayer) {
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

    public synchronized static void printChat(Map<String, Map<String, String>> chat) {
        List<String> dates = new ArrayList<>(chat.keySet());
        Collections.sort(dates);
        for (String date : dates) {
            System.out.print(date + " - ");
            Map<String, String> messages = chat.get(date);
            List<String> names = new ArrayList<>(messages.keySet());
            Collections.sort(names);
            for (String name : names) {
                System.out.println(name + ": " + messages.get(name));
            }
        }
    }

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

    public synchronized static void printPersonalGoal(String personalGoal) {
        System.out.println("Personal goal:\n" + personalGoal);
    }

    public synchronized static void printCommonGoals(List<String> commonGoals) {
        System.out.println("Common goals:");
        for (String goal : commonGoals) {
            System.out.println(goal);
        }
    }

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

    public synchronized static void printError(String message) {
        System.out.println(CliColor.RED_BOLD_BRIGHT + message + CliColor.RESET);
    }

    public synchronized static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("You may pick tiles with: " + "pick ROW1,COL1 ROW2,COL2 ..., " + "where ROWi and COLi are positive integers");
        System.out.println("You may put tiles with: " + "put COL INDEX1 INDEX2 ..., " + "where COL is the column of your bookshelf and INDEXi represents the i-th picked tile, the order of the indexes represents the order in which tiles will be put in your bookshelf (bottom to top)");
        System.out.println("You may send message to another player with: " + "sendmessage NICKNAME MESSAGE, " + "if NICKNAME is set to 'all' all players will receive the message");
        System.out.println("You may show the chat using: " + "showchat");
        System.out.println("You may show goal details using: " + "showgoal GOAL_NAME, " + "where GOAL_NAME is the name of the goal");
        System.out.println("You may show the current game using: " + "showgame");
        System.out.println("You may save the game using: " + "save SAVE_NAME, " + "where SAVE_NAME is the name of the game save, please note that you cannot override existing saves");
    }

    public synchronized static void printGoal(ClientGoalDetail goal) {
        if (goal == null) {
            System.out.println("Goal not found");
            return;
        }
        System.out.println("Showing goal: " + goal.getName());
        System.out.println("Description:\n" + goal.getDescription());
        // TODO: add a better representation and print it
        //System.out.println("Representation:\n" + goal.getRepresentation());
    }

    public synchronized static void printLoadedGameNames(List<String> gameNames) {
        System.out.println("Choose a name from the following list:");
        for (String gameName : gameNames) {
            System.out.println(gameName);
        }
    }
}
