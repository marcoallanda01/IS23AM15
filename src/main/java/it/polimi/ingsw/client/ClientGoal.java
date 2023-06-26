package it.polimi.ingsw.client;

/**
 * Class that represents a goal on the client side, used for parsing from json
 */
public class ClientGoal implements ClientGoalDetail {
    private String name;
    private String description;
    private String image;

    /**
     * Default constructor
     */
    public ClientGoal() {
        super();
    }
    /**
     * Getter for the name of the goal
     * @return the name of the goal
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name, mainly used in json parsing
     * @param name the name of the goal
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Getter for the description of the goal
     * @return the description of the goal
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter for description, mainly used in json parsing
     * @param description the description of the goal
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Getter for the image name of the goal
     * @return the image name of the goal
     */
    public String getImage() {
        return image;
    }
    /**
     * Setter for image, mainly used in json parsing
     * @param image the image name of the goal
     */
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ClientGoal{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}