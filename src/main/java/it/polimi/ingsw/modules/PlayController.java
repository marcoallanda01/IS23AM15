package it.polimi.ingsw.modules;

public class PlayController {
    private Game game;
    public Integer getPlayerPoints(String nickname) throws PlayerNotFoundException {
        return game.getPlayerPoints(nickname);
    }
}
