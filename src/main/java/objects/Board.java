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
     * @param size integer indicating the number of intersections where to place stones.
     */
    public Board(int size){
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
     * Getter method to retrieve the stone at the specified position on the game board.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @return int representing the value of the stone at the specified position.
     */
    public int getStone(int X, int Y){
        return grid[X][Y];
    }

    /**
     * Setter method to add a stone at the specified position.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @param color representing the color of the stone to place at the specified position.
     */
    public void addStone(int X, int Y, int color){
        grid[X][Y] = color;
    }

}
