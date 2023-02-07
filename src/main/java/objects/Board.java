package objects;

/**
 * Class that implements the board for the game Quentin.
 *
 * @author Gianluca Guglielmo
 */
public class Board implements FrozenBoard{
    private int[][] grid;
    private final int size;

    /**
     * Constructor method to create a new Board object.
     *
     * @param size integer indicating the number of intersections where to place stones.
     */
    public Board(int size){
        this.size = size;
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
    public void loadGrid(int[][] newGrid) {
        grid = newGrid;
    }

    /**
     * Setter method to add a stone at the specified position.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @param value representing the color of the stone to place at the specified position.
     */
    public void addStone(int X, int Y, int value){
        grid[X][Y] = value;
    }

    /**
     * Check if coordinates are within the limits of the grid.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @return true if the coordinates are within the limits of the grid.
     */
    public boolean checkInGrid(int X, int Y) {
        return X >= 0 && X < size && Y >= 0 && Y < size;
    }

    /**
     * Check if grid is empty in specified coordinates.
     * If coordinates are not within the limits of the grid, return true.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @return true if grid is empty in specified coordinates.
     */
    public boolean checkEmpty(int X, int Y) {
        if (!checkInGrid(X, Y)) return true;
        return getStone(X, Y) == 0;
    }

    /**
     * Getter method to retrieve a stone from coordinates.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @return int representing the color of the stone, 0 if empty or outside the grid.
     */
    public int getStone(int X, int Y){
        if (checkInGrid(X, Y)) return grid[X][Y];
        return 0;
    }

    /**
     * Method to invert the single initial black stone if the pie rule is invoked.
     */
    public void changeStonePieRule(){
        outerLoop:
        for(int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                if (getStone(i,j) == 1) {
                    addStone(i, j, 2);
                    break outerLoop;
                }
            }
        }
    }

}
