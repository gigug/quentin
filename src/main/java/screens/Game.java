package screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import objects.Board;
import players.Player;
import players.PlayerWrapper;

/**
 * Class that implements the board game Quentin.
 */
public class Game {
    // Declare players
    private final Player blackPlayer = new Player(1);
    private final Player whitePlayer = new Player(2);

    // Turn number
    private int turn;

    // Current player
    private PlayerWrapper currentPlayer;

    // Winner of the game
    private int winner;

    // If the game is finished or not
    private boolean finished;

    // Declare the board
    private final Board board;

    // Number of intersections where to place stones (= number of tiles + 1)
    private final int size;

    // Lists to hold the regions, chains, and territories
    private List<List<int[]>> regions = new ArrayList<>();
    private List<List<int[]>> chains = new ArrayList<>();
    private List<List<int[]>> territories = new ArrayList<>();
    private final Stack<int[][]> previousGridStack = new Stack<int[][]>();

    // Initialize directions used in the class methods
    int[][] directions = new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
    int[][] directionsDiagonal = new int[][]{{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

    /**
     * Constructor for the class Game.
     *
     * @param size int representing the size of the grid (number of intersections per side).
     */
    public Game(int size) {
        this.size = size;
        board = new Board(size);

        // initialize first player
        currentPlayer = new PlayerWrapper(blackPlayer);
    }

    /**
     * Set loaded player as current player.
     *
     * @param loadedPlayer loaded player from .game file.
     */
    public void loadCurrentPlayer(int loadedPlayer){
        currentPlayer = (loadedPlayer == 1) ? new PlayerWrapper(blackPlayer) : new PlayerWrapper(whitePlayer);
    }

    /**
     * Get current player's color.
     *
     * @return color of current player.
     */
    public int getCurrentPlayer() {
        return currentPlayer.getColor();
    }

    /**
     * Get opposite player's color.
     *
     * @return color of opposite player.
     */
    public int getOppositePlayer() {
        return (getCurrentPlayer() == 1) ? 2:1;
    }

    /**
     * Switch current player with opposite player.
     */
    public void switchPlayer() {
        currentPlayer = (getCurrentPlayer() == 1) ? new PlayerWrapper(whitePlayer) : new PlayerWrapper(blackPlayer);
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
        if (!checkInGrid(X, Y)){
            return true;
        }
        return getStone(X, Y) == 0;
    }

    /**
     * Check if any same-colored tiles are diagonally adjacent.
     * This method is needed to implement the rule that bans placing tiles
     * when both the following conditions are respected:
     * - the coordinates have a same-colored tile which is diagonally adjacent;
     * - there is no same-colored tile orthogonal to both the new coordinates
     *   and to the found same-colored tile;
     *
     * @return true if the previous rule does not apply, false otherwise.
     */
    public boolean checkDiagonal(int X, int Y) {

        int newX, newY;
        boolean valid = true;

        for (int[] dir : directionsDiagonal) {
            newX = X + dir[0];
            newY = Y + dir[1];

            // Check if stone has the same color as current player while the orthogonal ones are different
            if (getStone(newX, newY) == getCurrentPlayer() && getStone(newX, Y) != getCurrentPlayer() && getStone(X, newY) != getCurrentPlayer()) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    /**
     * Check if turn is passable.
     *
     * @return true if turn is passable.
     */
    public boolean checkPassable() {
        boolean passable = true;

        outerloop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (checkDiagonal(i, j) && checkEmpty(i, j)) {
                    passable = false;
                    break outerloop;
                }
            }
        }

        return passable;
    }

    /**
     * Method to check if the game has been won.
     */
    public void checkWin(){
        // Find chains
        findRegions(true);
        checkChains();
    }

    /**
     * Method to check if any chain reaches from one side of the grid to the other.
     */
    public void checkChains(){

        int color = 0;
        // A rare case in which two winners complete a winning chain at the same time is possible.
        // This List helps to detect this case.
        List<Integer> winners = new ArrayList<Integer>();

        int colorToUse;
        // Booleans indicating whether a chain touches any side of the grid
        boolean N, E, S, W;

        for(List<int[]> chain: chains) {
            N = E = S = W = false;

            // Iterate over elements of chain
            for (int[] element : chain) {
                color = getStone(element[0], element[1]);
                if (element[0] == 0) N = true;
                if (element[0] == size - 1) S = true;
                if (element[1] == 0) E = true;
                if (element[1] == size - 1) W = true;
            }

            // If chain touches opposite sides, assign winner and finish game
            if ((N && S) || (E && W)) {
                // stones belonging to the winning chains are colored differently to highlight them graphically
                colorToUse = (color == 1) ? 3 : 4;
                for (int[] element : chain) addStone(element[0], element[1], colorToUse);

                // If no winners up to this point, add winner
                if (winners.size() == 0) winners.add(color);
                // If two different winners, add second winner and break loop
                else if(winners.get(0) != color){
                    winners.add(color);
                    break;
                }
            }
        }
        // If one winner, assign winner
        if (winners.size() == 1) setWinner(winners.get(0));
        // If two winners, assign tie
        else if(winners.size() == 2) setWinner(0);
    }

    /**
     * Method to check if the game has been tied.
     */
    public void checkTie() {
        boolean isFull = true;

        // Simply checks if there is any empty spot
        if (!isFinished()){
            outerloop:
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (getStone(i, j) == 0) {
                        isFull = false;
                        break outerloop;
                    }
                }
            }
            if (isFull && !isFinished()) setWinner(0);
        }
    }

    /**
     * Method to find regions and chains on the grid.
     * They are not searched at the same time, the method needs to be called twice.
     *
     * @param chains boolean indicating if regions or chains need to be found.
     */
    public void findRegions(boolean chains) {

        boolean[][] visited = new boolean[size][size];

        int color = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!visited[i][j]){
                    // Start a new chain or region
                    List<int[]> region = new ArrayList<>();
                    if (chains && !checkEmpty(i, j)) {
                        color = getStone(i, j);
                        exploreRegion(i, j, visited, region, true, color);
                        addChain(region);
                    }
                    else if (!chains && checkEmpty(i, j)) {
                        exploreRegion(i, j, visited, region, false, color);
                        addRegion(region);
                    }
                }
            }
        }
    }

    /**
     * Support method for the findRegions method.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @param visited array indicating which coordinates have been visited.
     * @param region list of coordinates belonging to that region.
     * @param chains boolean indicating whether to find chains or regions.
     * @param color int indicating color of the analyzed coordinate.
     */
    private void exploreRegion(int X, int Y, boolean[][] visited, List<int[]> region, boolean chains, int color) {

        // If: searching for chains and found wrong color or searching for regions and not empty, return
        if ((chains && getStone(X, Y) != color) || (!chains && !checkEmpty(X, Y))){
            return;
        }

        // Mark the current tile as visited
        visited[X][Y] = true;

        // Add the current tile to the region
        region.add(new int[] {X, Y});

        int newX, newY;

        for (int[] dir: directions) {
            newX = X + dir[0];
            newY = Y + dir[1];

            // Explore coordinates not yet visited
            if (checkInGrid(newX, newY)){
                if (!visited[newX][newY]){
                    exploreRegion(newX, newY, visited, region, chains, color);
                }
            }
        }
    }

    /**
     * Method to empty regions, chains, territories and their colors when updating grid.
     */
    public void emptyRegions(){
        regions = new ArrayList<>();
        chains = new ArrayList<>();
        territories = new ArrayList<>();
    }

    /**
     * Method to transform eligible regions into territories.
     */
    public void validateTerritories(){

        int newX, newY, neighbors;
        boolean valid;

        // Iterate over regions
        for(List<int[]> region: getRegions()){

            // Indicates if region is territory
            valid = true;

            // Iterate over elements
            for(int[] element: region){
                neighbors = 0;

                for (int[] dir: directions) {
                    newX = element[0] + dir[0];
                    newY = element[1] + dir[1];

                    if (checkInGrid(newX, newY) && !checkEmpty(newX, newY)) neighbors += 1;
                }
                if (neighbors < 2){
                    valid = false;
                    break;
                }
            }

            // Promote region to territory
            if (valid) addTerritory(region);
        }
    }

    /**
     * Method to assign a color to a territory.
     */
    public void findTerritoryColors(){

        boolean found;
        int[] counts = new int[3];
        int color;

        for(List<int[]> territory: getTerritories()){
            List<int[]> listOccupiedNeighbors = new ArrayList<>();

            // Iterate over elements of territory to find every neighbor
            for(int[] element: territory){

                for (int[] dir: directions) {
                    final int newX = element[0] + dir[0];
                    final int newY = element[1] + dir[1];

                    if (checkInGrid(newX, newY) && !checkEmpty(newX, newY)){
                        // Check if coordinate was already included
                        found = listOccupiedNeighbors.stream().anyMatch(neighbor -> Arrays.equals(neighbor, new int[]{newX, newY}));
                        if (!found) {
                            listOccupiedNeighbors.add(new int[]{newX, newY});
                            counts[getStone(newX, newY)]++;
                        }
                    }
                }
            }

            color = counts[1] > counts[2] ? 1 : 2;
            if(counts[1] == counts[2]) color = 3 - getCurrentPlayer();
            colorTerritory(territory, color);
        }
    }

    /**
     * Color the territory.
     *
     * @param territory list of coordinates belonging to that territory.
     * @param color int representing the color of the territory.
     */

    public void colorTerritory(List<int[]> territory, int color){
        for(int[] element: territory) addStone(element[0], element[1], color);
    }

    /**
     * Getter method to retrieve a stone from coordinates.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @return int representing the color of the stone, 0 if empty or outside the grid.
     */
    public int getStone(int X, int Y){
        // If in grid return stone
        if (checkInGrid(X, Y)) return board.getStone(X, Y);

        // Else return empty
        return 0;

    }

    /**
     * Setter method to add a stone at the specified position.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @param color representing the color of the stone.
     */
    public void addStone(int X, int Y, int color){
        board.addStone(X, Y, color);
    }

    /**
     * Method to invert the single initial black stone if the pie rule is invoked.
     */
    public void changeStonePieRule(){
        outerloop:
        for(int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                if (getStone(i,j) == 1) {
                    addStone(i, j, 2);
                    break outerloop;
                }
            }
        }
    }

    /**
     * Setter method to add a region to the list of regions.
     *
     * @param region List<int[]> indicating the region to add.
     */
    public void addRegion(List<int[]> region){regions.add(region);}

    /**
     * Setter method to add a chain to the list of chains.
     *
     * @param chain List<int[]> indicating the region to add.
     */
    public void addChain(List<int[]> chain){
        chains.add(chain);
    }

    /**
     * Setter method to add a territory to the list of territories.
     *
     * @param territory List<int[]> indicating the region to add.
     */
    public void addTerritory(List<int[]> territory){
        territories.add(territory);
    }

    /**
     * Getter method to retrieve the finished attribute.
     *
     * @return finished boolean.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Setter method for winner.
     *
     * @param winner int indicating the winner of the game.
     */
    public void setWinner(int winner) {
        this.winner = winner;
        this.finished = true;
    }

    /**
     * Setter method for the board grid.
     *
     * @param newGrid int[][] representing the loaded grid.
     */
    public void setGrid(int[][] newGrid) {
        board.setGrid(newGrid);
    }

    /**
     * Getter method to retrieve the winner.
     *
     * @return winner int representing the winner.
     */
    public int getWinner(){
        return winner;
    }

    /**
     * Setter method to set the correct turn when loading a game.
     *
     * @param loadedTurn int representing the loaded turn.
     */
    public void setTurn(int loadedTurn) {
        turn = loadedTurn;
    }

    /**
     * Getter method to retrieve the turn.
     *
     * @return turn int representing the turn.
     */
    public int getTurn(){
        return turn;
    }

    /**
     * Method to increase the turn counter by one.
     */
    public void increaseTurn(){ 
        turn += 1;
    }

    /**
     * Method to decrease the turn counter by one.
     */
    public void decreaseTurn(){
        turn -= 1;
    }

    /**
     * Getter method to retrieve the size of the grid (number of intersections per side).
     *
     * @return size int representing the size of the grid.
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter method to retrieve the list of regions.
     *
     * @return regions List<List<int[]>>.
     */
    public List<List<int[]>> getRegions(){
        return regions;
    }

    /**
     * Getter method to retrieve the list of chains.
     *
     * @return chains List<List<int[]>>.
     */
    public List<List<int[]>> getChains(){
        return chains;
    }

    /**
     * Getter method to retrieve the list of territories.
     *
     * @return territories List<List<int[]>>.
     */
    public List<List<int[]>> getTerritories(){
        return territories;
    }

    /**
     * Getter method to retrieve the grid.
     *
     * @return grid int[][].
     */
    public int[][] getGrid(){return board.getGrid();}

    /**
     * Method to add a move to the undoStack.
     */
    public void addStack() {
        // Deep copy
        int[][] temporaryGrid = new int[size][size];
        for (int i = 0; i < size; i++) {
            temporaryGrid[i] = Arrays.copyOf(getGrid()[i], size);
        }
        previousGridStack.push(temporaryGrid);
    }

    /**
     * Method to undo moves.
     */
    public void undoMove() {
        setGrid(previousGridStack.pop());
        switchPlayer();
        decreaseTurn();
    }

    /**
     * Method to carry out every action needed at the end of a turn.
     */
    public void progress(){

        increaseTurn();
        findRegions(false);
        validateTerritories();
        findTerritoryColors();
        checkWin();
        checkTie();
        emptyRegions();
        switchPlayer();
    }


}