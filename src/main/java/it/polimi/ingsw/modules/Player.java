package it.polimi.ingsw.modules;

import java.util.List;
import java.util.Optional;

public class Player {
    private String userName;
    public Integer points;
    public BookShelf bookShelf;

    public Player(String userName) {
        this.userName = userName;
        this.points = 0;
        this.bookShelf = new BookShelf();
    }
    public Player(String userName, Integer points) {
        this.userName = userName;
        this.points = points;
    }
    public void addPoints(Integer points) {
        this.points += points;
    }

    public BookShelf getBookShelf() {
        return this.bookShelf;
    }
}
