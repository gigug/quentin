package players;

/**
 * Enum class to simplify the management of the PlayerColors.
 *
 * @author Gianluca Guglielmo
 */
public enum PlayerColor {
    BLACK(1), WHITE(2);
    private final int value;

    /**
     * Constructor for the class PlayerColor.
     *
     * @param value int indicating the numerical index associated to the color.
     */
    PlayerColor(int value) {
        this.value = value;
    }

    /**
     * Method to return the numerical index associated to the color.
     *
     * @return value.
     */
    public int getValue() {
        return value;
    }
}
