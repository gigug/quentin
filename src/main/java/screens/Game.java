package screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import objects.Board;
import objects.FrozenBoard;
import players.PlayerColor;

/**
 * Class that implements the board game Quentin.
 *
 * @author Gianluca Guglielmo
 */
public class Game {
    // Declare players
    final static PlayerColor BLACK_PLAYER = PlayerColor.BLACK;
    final static PlayerColor WHITE_PLAYER = PlayerColor.WHITE;
    private PlayerColor currentPlayer;
    private int winner;
    private int turn;
    private boolean gameFinished;
    private final Board board;

    // Number of intersections where to place stones (= number of tiles + 1)
    private final int size;

    // Lists to hold the regions, chains, and territories
    private List<List<int[]>> regions = new ArrayList<>();
    private List<List<int[]>> chains = new ArrayList<>();
    private List<List<int[]>> territories = new ArrayList<>();
    private Stack<int[][]> previousGridStack = new Stack<int[][]>();

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
        currentPlayer = BLACK_PLAYER;
    }

    public Game(int size, int[][] grid, PlayerColor currentPlayer, int turn, Stack<int[][]> previousGridStack){
        this(size);
        this.setGrid(grid);
        this.setCurrentPlayer(currentPlayer);
        this.setTurn(turn);
        this.setPreviousGridStack(previousGridStack);
    }

    /**
     * Return FrozenBoard for safe handling of Board.
     *
     * @return FrozenBoard.
     */
    public FrozenBoard getFrozenBoard() {
        return board;
    }

    /**
     * Set loaded player as current player.
     *
     * @param loadedPlayer loaded player from .game file.
     */
    public void setCurrentPlayer(PlayerColor loadedPlayer){
        currentPlayer = loadedPlayer;
    }

    /**
     * Get current player's color.
     *
     * @return color of current player.
     */
    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get current player's value.
     *
     * @return value of current player.
     */
    public int getCurrentPlayerValue() {
        return currentPlayer.getValue();
    }

    /**
     * Get opposite player's color.
     *
     * @return color of opposite player.
     */
    public int getOppositePlayer() {
        return 3 - getCurrentPlayerValue();
    }

    /**
     * Switch current player with opposite player.
     */
    public void switchPlayer() {
        currentPlayer = (getCurrentPlayer() == BLACK_PLAYER) ? WHITE_PLAYER : BLACK_PLAYER;
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
            if (board.getStone(newX, newY) == getCurrentPlayerValue() && board.getStone(newX, Y) != getCurrentPlayerValue() && board.getStone(X, newY) != getCurrentPlayerValue()) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    /**
     * Method to check whether the hovered cell can host a player stone.
     *
     * @param X int representing the x position of the stone.
     * @param Y int representing the y position of the stone.
     * @return boolean indicating whether the stone can be placed.
     */
    public boolean checkPlaceable(int X, int Y){
        return (board.checkEmpty(X, Y) && checkDiagonal(X, Y));
    }

    /**
     * Check if turn is passable.
     *
     * @return true if turn is passable.
     */
    public boolean checkPassable() {
        boolean passable = true;

        outerLoop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (checkDiagonal(i, j) && board.checkEmpty(i, j)) {
                    passable = false;
                    break outerLoop;
                }
            }
        }

        return passable;
    }

    /**
     * Method to check if the game has been won.
     */
    public void checkWin(){
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
                color = board.getStone(element[0], element[1]);
                if (element[0] == 0) N = true;
                if (element[0] == size - 1) S = true;
                if (element[1] == 0) E = true;
                if (element[1] == size - 1) W = true;
            }

            // If chain touches opposite sides, assign winner and finish game
            if ((N && S) || (E && W)) {
                // stones belonging to the winning chains are colored differently to highlight them graphically
                colorToUse = (color == 1) ? 3 : 4;
                for (int[] element : chain) board.addStone(element[0], element[1], colorToUse);

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
        if (!isGameFinished()){
            outerLoop:
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board.getStone(i, j) == 0) {
                        isFull = false;
                        break outerLoop;
                    }
                }
            }
            if (isFull && !isGameFinished()) setWinner(0);
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
                    if (chains && !board.checkEmpty(i, j)) {
                        color = board.getStone(i, j);
                        exploreRegion(i, j, visited, region, true, color);
                        addChain(region);
                    }
                    else if (!chains && board.checkEmpty(i, j)) {
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
        if ((chains && board.getStone(X, Y) != color) || (!chains && !board.checkEmpty(X, Y))) return;

        // Mark the current tile as visited
        visited[X][Y] = true;

        // Add the current tile to the region
        region.add(new int[] {X, Y});

        int newX, newY;

        for (int[] dir: directions) {
            newX = X + dir[0];
            newY = Y + dir[1];

            // Explore coordinates not yet visited
            if (board.checkInGrid(newX, newY)){
                if (!visited[newX][newY]) exploreRegion(newX, newY, visited, region, chains, color);
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

                    if (board.checkInGrid(newX, newY) && !board.checkEmpty(newX, newY)) neighbors += 1;
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

                    if (board.checkInGrid(newX, newY) && !board.checkEmpty(newX, newY)){
                        // Check if coordinate was already included
                        found = listOccupiedNeighbors.stream().anyMatch(neighbor -> Arrays.equals(neighbor, new int[]{newX, newY}));
                        if (!found) {
                            listOccupiedNeighbors.add(new int[]{newX, newY});
                            counts[board.getStone(newX, newY)]++;
                        }
                    }
                }
            }

            color = counts[1] > counts[2] ? 1 : 2;
            if(counts[1] == counts[2]) color = 3 - getCurrentPlayerValue();
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
        for(int[] element: territory) board.addStone(element[0], element[1], color);
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
    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Setter method for winner.
     *
     * @param winner int indicating the winner of the game.
     */
    public void setWinner(int winner) {
        this.winner = winner;
        this.gameFinished = true;
    }

    /**
     * Setter method for the board grid.
     *
     * @param newGrid int[][] representing the loaded grid.
     */
    public void setGrid(int[][] newGrid) {
        board.loadGrid(newGrid);
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
     * Getter method to retrieve the stack of previous moves
     */
    public Stack<int[][]> getPreviousGridStack(){
        return previousGridStack;
    }


    /**
     * Setter method to load the stack of previous moves.
     *
     * @param newPreviousGridStack Stack loaded from file.
     */
    public void setPreviousGridStack(Stack<int[][]> newPreviousGridStack){
        previousGridStack = newPreviousGridStack;
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
        int[][] tempGrid = new int[size][size];
        for (int i = 0; i < size; i++) tempGrid[i] = Arrays.copyOf(getGrid()[i], size);
        previousGridStack.push(tempGrid);
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
     * Method to carry out the sequence of actions needed at the end of a turn.
     */
    public void progress(int X, int Y){
        addStack();
        board.addStone(X, Y, getCurrentPlayerValue());
        increaseTurn();
        findRegions(false);
        validateTerritories();
        findTerritoryColors();
        checkWin();
        checkTie();
        emptyRegions();
        switchPlayer();
    }

    /**
     * Method to apply the sequence of actions needed for the pie rule.
     */
    public void pieRule() {
        addStack();
        board.changeStonePieRule();
        increaseTurn();
        switchPlayer();
    }
}