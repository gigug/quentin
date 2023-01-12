package players;

/**
 * This class implements the Player object.
 *
 * @author Gianluca Guglielmo
 */
public class Player {

    // color of the player
    private final int color;

    /**
     * Constructor method to create a new Player object.
     *
     * @param color integer representing the color of the player
     */
    public Player (int color){
        this.color = color;
    }

    /**
     * Getter method to retrieve the color of the player
     *
     * @return int representing the color of the player
     */
    public int getColor(){
        return this.color;
    }
}