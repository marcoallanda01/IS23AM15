package it.polimi.ingsw.server.model;

public class Token {
    private int points;
    private int number;

    public Token(int points, int number) {
        this.points = points;
        this.number = number;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
