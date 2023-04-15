package it.polimi.ingsw.model;

public class Player {
    private final String userName;
    private Integer points;
    private BookShelf bookShelf;
    private boolean firstToFinish = false;
    private boolean isPlaying = true;
    private boolean fullBookShelf = false;
    /**
     * Create a new player
     * @param userName the username of the player
     */
    public Player(String userName) {
        this.userName = userName;
        this.points = 0;
        this.bookShelf = new BookShelf();
    }
    /**
     * Create a new player
     * @param userName the username of the player
     * @param points the points of the player
     */
    public Player(String userName, Integer points) {
        this.userName = userName;
        this.points = points;
    }
    /**
     * Get the points of the player
     * @return the points of the player
     */
    public Integer getPoints() {
        return this.points;
    }
    /**
     * Add points to the player
     * @param points the points to add
     */
    public void addPoints(Integer points) {
        this.points += points;
    }
    /**
     * Get the bookshelf of the player
     * @return the bookshelf of the player
     */
    public BookShelf getBookShelf() {
        return this.bookShelf;
    }
    /**
     * Get the username of the player
     * @return the username of the player
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Get the value of isPlaying
     * @return the value of isPlaying
     */
    public boolean isPlaying() {
        return this.isPlaying;
    }
    /**
     * Set the value of isPlaying
     * @param playing the new value of isPlaying
     */
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }
    /**
     * Get the value of firstToFinish
     * @return the value of firstToFinish
     */
    public boolean isFirstToFinish() {
        return this.firstToFinish;
    }
    /**
     * Set the value of firstToFinish
     * @param firstToFinish the new value of firstToFinish
     */
    public void setFirstToFinish(boolean firstToFinish) {
        this.firstToFinish = firstToFinish;
    }
    /**
     * Get the value of fullBookShelf
     * @return the value of fullBookShelf
     */
    public boolean hasFinished() {
        return this.fullBookShelf;
    }
    /**
     * Set the value of fullBookShelf
     * @param fullBookShelf the new value of fullBookShelf
     */
    public void setFullBookShelf(boolean fullBookShelf) {
        this.fullBookShelf = fullBookShelf;
    }
}
