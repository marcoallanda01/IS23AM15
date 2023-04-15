package it.polimi.ingsw.model;

public class Player {
    private final String userName;
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
        this.bookShelf = new BookShelf();
    }
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
