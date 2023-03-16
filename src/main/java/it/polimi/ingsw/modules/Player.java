package it.polimi.ingsw.modules;

public class Player {
    private String userName;
    public Integer points;

    public Player(String userName) {
        this.userName = userName;
        this.points = 0;
    }
    public Player(String userName, Integer points) {
        this.userName = userName;
        this.points = points;
    }
    public void addPoints(Integer points) {
        this.points += points;
    }
}
