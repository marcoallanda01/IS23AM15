package it.polimi.ingsw.model;

public class Token {
    private Integer points;
    private Integer number;

    public Token(Integer points, Integer number) {
        this.points = points;
        this.number = number;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
