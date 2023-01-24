package players;

/**
 * This class implements the PlayerWrapper object.
 *
 * @author Gianluca Guglielmo
 */
public class PlayerWrapper{

    // Player
    private final Player player;

    /**
     * Constructor method to create a new PlayerWrapper object.
     *
     * @param player representing the current player.
     */
    public PlayerWrapper(Player player){
        this.player = player;
    }

    /**
     * Getter method to retrieve the color of the current player.
     *
     * @return int representing the color of the player.
     */
    public int getColor() {
        return player.getColor();
    }
}
