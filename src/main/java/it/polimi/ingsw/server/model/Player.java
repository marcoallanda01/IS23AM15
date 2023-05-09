package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.PushNotificationController;
import it.polimi.ingsw.server.listeners.PlayerListener;
import it.polimi.ingsw.server.listeners.StandardListenable;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Objects;

public class Player implements StandardListenable, PostProcessable {
    private final String userName;
    private final BookShelf bookShelf;
    private boolean firstToFinish = false;
    private transient boolean isPlaying = true;
    private boolean fullBookShelf = false;
    private int points;
    private transient PropertyChangeSupport propertyChangeSupport;

    /**
     * Create a new player
     * @param userName the username of the player
     */
    public Player(String userName) {
        this.userName = userName;
        this.bookShelf = new BookShelf();
        this.points = 0;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Insert tiles in player bookshelf
     * @return result from @code{ BookShelf.insertTiles() }
     */
    public boolean insertTiles(List<Tile> tiles, int column){
        boolean res = false;
        List<Tile> updatedTiles = this.bookShelf.insertTiles(tiles, column);
        if(updatedTiles != null){
            res = true;
            this.propertyChangeSupport.firePropertyChange("bookShelfChange", this, updatedTiles);
        }
        return res;
    }

    /**
     * Get player's BookShelf
     * @return player's BookShelf
     */
    public BookShelf getBookShelf() {
        return this.bookShelf;
    }
    /**
     * Get the username of the player
     * @return the username of the player
     */
    public String getUserName() {
        return new String(this.userName);
    }

    /**
     * Get the value of isPlaying
     * @return the value of isPlaying
     */
    public boolean isPlaying() {
        return this.isPlaying;
    }

    /**
     * Set the value of isPlaying, used in deserialization
     * @param playing the new value of isPlaying
     */
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;

    }

    /**
     * call if from now on player turn is to skip
     */
    public void goToWc(){
        setPlaying(false);
        this.propertyChangeSupport.firePropertyChange("playingState", null, this.isPlaying);
    }

    /**
     * call if from now on player turn is not to skip
     */
    public void backFromWc(){
        setPlaying(true);
        this.propertyChangeSupport.firePropertyChange("playingState", null, this.isPlaying);
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


    /**
     * Set point
     * @param points points to set
     */
    public void setPoints(int points) {
        propertyChangeSupport.firePropertyChange("pointsUpdate", this.points, points);
        this.points = points;
    }

    /**
     * Set standard player listener
     */
    public void setStandardListener(PushNotificationController pnc){
        this.propertyChangeSupport.addPropertyChangeListener(new PlayerListener(pnc));
    }

    @Override
    public String toString() {
        return "Player{" +
                "userName='" + userName + '\'' +
                ", bookShelf=" + bookShelf +
                ", firstToFinish=" + firstToFinish +
                ", isPlaying=" + isPlaying +
                ", fullBookShelf=" + fullBookShelf +
                ", propertyChangeSupport=" + propertyChangeSupport +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return firstToFinish == player.firstToFinish && isPlaying == player.isPlaying && fullBookShelf == player.fullBookShelf && Objects.equals(userName, player.userName) && Objects.equals(bookShelf, player.bookShelf) && Objects.equals(propertyChangeSupport, player.propertyChangeSupport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, bookShelf, firstToFinish, isPlaying, fullBookShelf, propertyChangeSupport);
    }

    @Override
    public void gsonPostProcess() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Force listeners notification
     */
    public void notifyListeners(){
        this.propertyChangeSupport.firePropertyChange("pointsUpdate", -1, points);
        this.propertyChangeSupport.firePropertyChange("bookShelfChange", this,
                this.bookShelf.getAllTiles());
    }
}
