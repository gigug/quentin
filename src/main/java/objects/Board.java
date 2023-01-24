package objects;

/**
 * Class that implements the board for the game Quentin.
 */
public class Board {

    // Game board
    private int[][] grid;

    /**
     * Constructor method to create a new Board object.
     *
     * @param size integer indicating the number of intersections where to place pawns.
     */
    public Board(int size){
        // size = number of intersections (per side) where to place pawns
        // initialize grid elements to 0
        grid = new int[size][size];
    }

    /**
     * Getter method to retrieve the game board grid.
     *
     * @return 2D integer array representing the game board.
     */
    public int[][] getGrid(){
        return grid;
    }

    /**
     * Method to update the current game board grid with a new grid when loading a game.
     *
     * @param newGrid 2D int array representing the new game board.
     */
    public void setGrid(int[][] newGrid) {
        grid = newGrid;
    }

    /**
     * Getter method to retrieve the piece at the specified position on the game board.
     *
     * @param X int representing the x position of the piece.
     * @param Y int representing the y position of the piece.
     * @return int representing the value of the piece at the specified position.
     */
    public int getPiece(int X, int Y){
        return grid[X][Y];
    }

    /**
     * Setter method to add a piece at the specified position.
     *
     * @param X int representing the x position of the piece.
     * @param Y int representing the y position of the piece.
     * @param color representing the color of the piece to place at the specified position.
     */
    public void addPiece(int X, int Y, int color){
        grid[X][Y] = color;
    }

}
