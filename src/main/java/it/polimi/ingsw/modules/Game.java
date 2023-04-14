package it.polimi.ingsw.modules;

import java.util.ArrayList;
import java.util.List;

public class Game{
    private final List<Player> players = new ArrayList<>();
    private GoalManager goalManager;
    public Integer getPlayerPoints(String nickname) throws PlayerNotFoundException {
        Player player = this.getPlayerFromNickname(nickname);
        return goalManager.getPlayerPoints(player) + (player.isFirstToFinish() ? 1 : 0);
    }

    private Player getPlayerFromNickname(String nickname) throws PlayerNotFoundException {
        try {
            return players.stream().filter(player -> player.getUserName().equals(nickname)).findAny().get();
        } catch (Exception e) {
            throw new PlayerNotFoundException();
        }
    }
}
