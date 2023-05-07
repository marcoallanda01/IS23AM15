package it.polimi.ingsw.client;

public class CLIRenderer {
    private String gameScreen;
    private String lobbyScreen;

    public CLIRenderer() {
        this.gameScreen = null;
        this.lobbyScreen = null;
    }

    public void printGame(){
        if(this.gameScreen == null){
            this.gameScreen = """
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
        }
        System.out.println(this.gameScreen);
    }

    public void printLobby(){
        if(this.lobbyScreen == null){
            this.lobbyScreen = "Lobby Screen";
        }
        System.out.println(this.lobbyScreen);
    }
}